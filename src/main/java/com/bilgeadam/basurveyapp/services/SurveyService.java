package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.EmailService;
import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.SurveyByClassroomQuestionAnswersResponseDto;
import com.bilgeadam.basurveyapp.dto.response.SurveyByClassroomQuestionsResponseDto;
import com.bilgeadam.basurveyapp.dto.response.SurveyByClassroomResponseDto;
import com.bilgeadam.basurveyapp.dto.response.SurveyResponseDto;
import com.bilgeadam.basurveyapp.entity.*;
import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import com.bilgeadam.basurveyapp.entity.enums.Role;
import com.bilgeadam.basurveyapp.exceptions.custom.AlreadyAnsweredSurveyException;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionsAndResponsesDoesNotMatchException;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.UserInsufficientAnswerException;
import com.bilgeadam.basurveyapp.repositories.ClassroomRepository;
import com.bilgeadam.basurveyapp.repositories.ResponseRepository;
import com.bilgeadam.basurveyapp.repositories.SurveyRepository;
import com.bilgeadam.basurveyapp.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final ClassroomRepository classroomRepository;
    private final ResponseRepository responseRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtService jwtService;

    public List<SurveyResponseDto> getSurveyList() {
        List<Survey> surveys = surveyRepository.findAllActive();
        return surveys.stream().map(survey ->
                SurveyResponseDto.builder()
                        .surveyOid(survey.getOid())
                        .surveyTitle(survey.getSurveyTitle())
                        .courseTopic(survey.getCourseTopic())
                        .build()
        ).collect(Collectors.toList());
    }

    public Page<Survey> getSurveyPage(Pageable pageable) {
        return surveyRepository.findAllActive(pageable);
    }

    public Boolean create(SurveyCreateRequestDto dto) {
        try {
            Survey survey = Survey.builder()
                    .surveyTitle(dto.getSurveyTitle())
                    .courseTopic(dto.getCourseTopic())
                    .build();
            surveyRepository.save(survey);
        } catch (Exception e) {
            throw new EntityExistsException("This survey title already in use.");
        }
        return true;
    }

    public Survey update(Long surveyId, SurveyUpdateRequestDto dto) {

        Optional<Survey> surveyToBeUpdated = surveyRepository.findActiveById(surveyId);
        if (surveyToBeUpdated.isEmpty()) {
            throw new ResourceNotFoundException("Survey is not found");
        }
        surveyToBeUpdated.get().setSurveyTitle(dto.getSurveyTitle());
        return surveyRepository.save(surveyToBeUpdated.get());
    }

    public void delete(Long surveyId) {

        Optional<Survey> surveyToBeDeleted = surveyRepository.findActiveById(surveyId);
        if (surveyToBeDeleted.isEmpty()) {
            throw new ResourceNotFoundException("Survey is not found");
        }
        surveyRepository.softDelete(surveyToBeDeleted.get());
    }

    public Survey findByOid(Long surveyId) {

        Optional<Survey> surveyById = surveyRepository.findActiveById(surveyId);
        if (surveyById.isEmpty()) {
            throw new ResourceNotFoundException("Survey is not found");
        }
        return surveyById.get();
    }

    public Boolean responseSurveyQuestions(String token, SurveyResponseQuestionRequestDto dto, HttpServletRequest request) {
        if (!jwtService.isSurveyEmailTokenValid(token)) {
            throw new AccessDeniedException("Invalid token");
        }

        Long surveyOid = jwtService.extractSurveyOid(token);

        Survey survey = surveyRepository.findActiveById(surveyOid)
                .orElseThrow(() -> new ResourceNotFoundException("Survey is not Found."));

        if (Boolean.FALSE.equals(crossCheckSurveyQuestionsAndCreateResponses(survey, dto.getCreateResponses()))) {
            throw new UserInsufficientAnswerException("User must response all the questions.");
        }

        String userEmail = jwtService.extractEmail(token);

        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User is not found"));

        // authentication ******
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                currentUser,
                currentUser.getOid(),
                currentUser.getAuthorities()
        );
        authenticationToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // authentication ******

        List<Long> participantIdList = survey.getUsers()
                .parallelStream()
                .map(User::getOid)
                .toList();
        if (participantIdList.contains(currentUser.getOid())) {
            throw new AlreadyAnsweredSurveyException("User cannot answer a survey more than once.");
        }

        List<Question> surveyQuestions = survey.getQuestions();

        List<Response> responses = dto.getCreateResponses().keySet()
                .parallelStream()
                .map((id -> Response.builder()
                        .responseString(dto.getCreateResponses().get(id).trim())
                        .question(surveyQuestions
                                .stream()
                                .filter(question -> question.getOid().equals(id))
                                .findAny()
                                .orElse(null))
                        .user(currentUser)
                        .build()))
                .toList();

        for (Response response : responses) {
            Optional<Question> questionOptional = surveyQuestions
                    .parallelStream()
                    .filter(question -> question.getOid().equals(response.getQuestion().getOid()))
                    .findAny();
            questionOptional.ifPresent(question -> question.getResponses().add(response));
        }

        survey.getUsers().add(currentUser);

        currentUser.getSurveys().add(survey);

        surveyRepository.save(survey);
        return true;
    }

    public Survey updateSurveyAnswers(Long surveyId, SurveyUpdateResponseRequestDto dto) {
        Survey survey = surveyRepository.findActiveById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey is not Found"));

        if (Boolean.FALSE.equals(crossCheckSurveyQuestionsAndUpdateResponses(survey, dto.getUpdateResponseMap()))) {
            throw new QuestionsAndResponsesDoesNotMatchException("Questions does not match with responses.");
        }
        Optional<Long> currentUserIdOptional = Optional.of((Long) SecurityContextHolder.getContext().getAuthentication().getCredentials());
        Long currentUserId = currentUserIdOptional.orElseThrow(() -> new ResourceNotFoundException("Token does not contain User Info"));
        List<Response> currentUserResponses = survey.getQuestions()
                .stream()
                .flatMap(question -> question.getResponses().stream())
                .filter(response -> response.getUser().getOid().equals(currentUserId))
                .collect(Collectors.toList());

        currentUserResponses
                .parallelStream()
                .filter(response -> dto.getUpdateResponseMap().containsKey(response.getOid()))
                .forEach(response -> response.setResponseString(dto.getUpdateResponseMap().get(response.getOid())));
        responseRepository.saveAll(currentUserResponses);
        return survey;
    }

    public Boolean assignSurveyToClassroom(SurveyAssignRequestDto dto) throws MessagingException {
        Survey survey = surveyRepository.findActiveById(dto.getSurveyId())
                .orElseThrow(() -> new ResourceNotFoundException("Survey is not Found"));
        Classroom classroom = classroomRepository.findActiveByName(dto.getClassroomName())
                .orElseThrow(() -> new ResourceNotFoundException("Classroom is not Found"));

        survey.getClassrooms().add(classroom);

        /*
         * Email service will be scheduled with accordance startDate of Survey.
         */
//        Map<String,String> emailTokenMap = classroom.getUsers()
//            .parallelStream()
//            .collect(Collectors.toMap(User::getEmail, user -> jwtService.generateMailToken(user.getEmail(),survey.getOid())));
//        emailService.sendSurveyMail(emailTokenMap);

        for (User user : classroom.getUsers()) {
            String jwtToken = jwtService.generateSurveyEmailToken(dto.getSurveyId(), user.getEmail(), dto.getDays());
            emailService.sendSurveyMail(user.getEmail(), jwtToken);
        }

        surveyRepository.save(survey);

        return true;
    }

    public List<SurveyByClassroomResponseDto> findByClassroomOid(Long classroomOid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("authentication failure.");
        }
        if ("anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("authentication failure.");
        }
        Long userOid = (Long) authentication.getCredentials();
        User user = userRepository.findActiveById(userOid).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

        if (user.getRole() == Role.ASSISTANT_TRAINER || user.getRole() == Role.MASTER_TRAINER) {
            Classroom classroom = classroomRepository.findActiveById(classroomOid).orElseThrow(() -> new ResourceNotFoundException("Classroom does not exist"));
            if (!classroom.getUsers().contains(user)) {
                throw new AccessDeniedException("authentication failure.");
            }
        }
        Optional<Classroom> classroomOptional = classroomRepository.findActiveById(classroomOid);
        if (classroomOptional.isEmpty()) {
            throw new ResourceNotFoundException("Classroom is not found.");
        }
        List<Survey> surveyList = surveyRepository.findAllActive();
        List<Survey> surveysWithTheOidsOfTheClasses = surveyList
                .stream()
                .filter(survey -> survey.getClassrooms()
                        .stream()
                        .map(BaseEntity::getOid)
                        .toList().contains(classroomOptional.get().getOid()))
                .toList();

        return mapToSurveyByClassroomResponseDtoList(surveysWithTheOidsOfTheClasses);
    }


    private Boolean crossCheckSurveyQuestionsAndCreateResponses(Survey survey, Map<Long, String> getCreateResponses) {

        Set<Long> surveyQuestionIdSet = survey.getQuestions()
                .parallelStream()
                .map(Question::getOid)
                .collect(Collectors.toSet());
        Set<Long> createResponseQuestionIdSet = getCreateResponses.keySet();

        return surveyQuestionIdSet.equals(createResponseQuestionIdSet);
    }

    private Boolean crossCheckSurveyQuestionsAndUpdateResponses(Survey survey, Map<Long, String> updateResponseMap) {
        Set<Long> surveyQuestionIdSet = survey.getQuestions()
                .parallelStream()
                .map(Question::getOid)
                .collect(Collectors.toSet());
        Set<Long> updateResponseQuestionIdSet = updateResponseMap.keySet();

        return surveyQuestionIdSet.containsAll(updateResponseQuestionIdSet);
    }


    // survey listesini -- survey dto list'e mapleyen method
    public List<SurveyByClassroomResponseDto> mapToSurveyByClassroomResponseDtoList(List<Survey> surveys) {
        return surveys.stream()
                .map(survey -> {
                    SurveyByClassroomResponseDto surveyDto = new SurveyByClassroomResponseDto();
                    surveyDto.setSurveyOid(survey.getOid());
                    surveyDto.setSurveyTitle(survey.getSurveyTitle());
                    surveyDto.setCourseTopic(survey.getCourseTopic());

                    List<SurveyByClassroomQuestionsResponseDto> questionDtoList = survey.getQuestions().stream()
                            .map(question -> {
                                SurveyByClassroomQuestionsResponseDto questionDto = new SurveyByClassroomQuestionsResponseDto();
                                questionDto.setQuestionOid(question.getOid());
                                questionDto.setQuestionString(question.getQuestionString());

                                List<SurveyByClassroomQuestionAnswersResponseDto> responseDtoList = question.getResponses().stream()
                                        .map(answer -> {
                                            SurveyByClassroomQuestionAnswersResponseDto responseDto = new SurveyByClassroomQuestionAnswersResponseDto();
                                            responseDto.setResponseOid(answer.getOid());
                                            responseDto.setResponseString(answer.getResponseString());
                                            return responseDto;
                                        })
                                        .collect(Collectors.toList());

                                questionDto.setResponseDtoList(responseDtoList);
                                return questionDto;
                            })
                            .collect(Collectors.toList());

                    surveyDto.setQuestionDtoList(questionDtoList);
                    return surveyDto;
                })
                .collect(Collectors.toList());
    }

}