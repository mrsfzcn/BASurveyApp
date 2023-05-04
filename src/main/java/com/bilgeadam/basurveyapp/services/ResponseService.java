package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.dto.request.FindAllResponsesOfUserRequestDto;
import com.bilgeadam.basurveyapp.dto.request.ResponseRequestDto;
import com.bilgeadam.basurveyapp.dto.request.ResponseRequestSaveDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyUpdateResponseRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AnswerResponseDto;
import com.bilgeadam.basurveyapp.entity.*;
import com.bilgeadam.basurveyapp.exceptions.custom.*;
import com.bilgeadam.basurveyapp.mapper.ResponseMapper;
import com.bilgeadam.basurveyapp.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ResponseService {
    private final ResponseRepository responseRepository;
    private final QuestionService questionService;
    private final UserService userService;
    private final JwtService jwtService;
    private final StudentService studentService;
    private final SurveyService surveyService;

    public void createResponse(ResponseRequestSaveDto responseRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("authentication failure.");
        }
        if ("anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("authentication failure.");
        }
        Long userOid = (Long) authentication.getCredentials();
        userService.findActiveById(userOid).orElseThrow(() -> new UserDoesNotExistsException("User does not exist"));

        Response response = ResponseMapper.INSTANCE.toResponse(responseRequestDto, questionService
                .findActiveById(responseRequestDto
                        .getQuestionOid()).orElseThrow(() -> new QuestionNotFoundException("question not found")), userService.findActiveById(userOid).orElseThrow(() -> new ResourceNotFoundException("User does not exist")));
        responseRepository.save(response);
    }

    public void updateResponse(ResponseRequestDto responseRequestDto) {
        Optional<Response> updatedResponse = responseRepository.findActiveById(responseRequestDto.getResponseOid());
        if (updatedResponse.isEmpty()) {
            throw new ResponseNotFoundException("There's a error while finding response");
        } else {
            updatedResponse.get().setResponseString(responseRequestDto.getResponseString());
            responseRepository.save(updatedResponse.get());
        }
    }

    public AnswerResponseDto findByIdResponse(Long responseOid) {
        Optional<Response> response = responseRepository.findActiveById(responseOid);
        if (response.isEmpty()) {
            throw new ResponseNotFoundException("There's a error while finding response");
        }
        return ResponseMapper.INSTANCE.toAnswerResponseDto(response.get());
//                AnswerResponseDto.builder()
//                .responseString(response.get().getResponseString())
//              .userOid(response.get().getUser().getOid())
//                .questionOid(response.get().getQuestion().getOid())
//                .build();
    }

    public List<AnswerResponseDto> findAll() {
        ResponseMapper responseMapper = ResponseMapper.INSTANCE;
        List<Response> findAllList = responseRepository.findAllActive();
        //        List<AnswerResponseDto> responseDtoList = new ArrayList<>();
//        findAllList.forEach(response ->
//                responseDtoList.add(AnswerResponseDto.builder()
//                        .responseString(response.getResponseString())
//                        .userOid(response.getUser().getOid())
//                        .questionOid(response.getQuestion().getOid())
//                        .build()));
        return findAllList.stream()
                .map(responseMapper::toAnswerResponseDto).collect(Collectors.toList());
    }

    public Boolean deleteResponseById(Long responseOid) {
        Optional<Response> response = responseRepository.findActiveById(responseOid);
        if (response.isEmpty()) {
            throw new ResponseNotFoundException("There's a error while finding response");
        } else {
            return responseRepository.softDeleteById(response.get().getOid());
        }
    }

    public Boolean saveAll(String token, List<ResponseRequestSaveDto> responseRequestSaveDtoList) {
        User user = userService.findByEmail(jwtService.extractEmail(token)).orElseThrow(() -> new ResourceNotFoundException("No such user."));// tokendan gelen id var gibi kabul edildi.Mail üzerinden yapıdı.
        Survey survey = surveyService.findActiveById(jwtService.extractSurveyOid(token)).orElseThrow(() -> new ResourceNotFoundException("No such survey."));
        if (!responseRepository.isSurveyAnsweredByUser(user.getOid(), survey.getOid())) {
            Map<Long, Question> questionMap = new HashMap<>();
            responseRequestSaveDtoList.forEach(response -> {
                Question question = questionMap.computeIfAbsent(response.getQuestionOid(),
                        id -> questionService.findActiveById(id)
                                .orElseThrow(() -> new QuestionNotFoundException("Question not found")));
                Response newResponse = Response.builder()
                        .responseString(response.getResponseString())
                        .question(question)
                        .survey(surveyService.findActiveById(survey.getOid())
                                .orElseThrow(() -> new SurveyNotFoundException("Survey not found")))
                        .user(user)
                        .build();
                Optional<Student> student = studentService.findByUser(user);
                student.get().getSurveysAnswered().add(survey);
                survey.getStudentsWhoAnswered().add(student.get());
                studentService.save(student.get());
                responseRepository.save(newResponse);
                question.getResponses().add(newResponse);
                questionService.save(question);
                survey.getResponses().add(newResponse);
                surveyService.save(survey);
            });
            return true;
        } else {
            return false;
        }
    }

    public List<AnswerResponseDto> findAllResponsesOfUserFromSurvey(FindAllResponsesOfUserRequestDto dto) {
        ResponseMapper responseMapper = ResponseMapper.INSTANCE;
        userService.findByEmail(dto.getUserEmail()).orElseThrow(() -> new UserDoesNotExistsException("User does not exists or deleted."));
        Survey survey = surveyService.findActiveById(dto.getSurveyOid()).orElseThrow(() -> new ResourceNotFoundException("Survey does not exists or deleted."));
        return responseRepository
                .findAllResponsesOfUserFromSurvey(dto.getUserEmail(), questionService.findSurveyQuestionOidList(survey.getOid()))
                .stream()
                .map(responseMapper::toAnswerResponseDto).collect(Collectors.toList());
//                        -> AnswerResponseDto.builder()
//                        .responseString(response.getResponseString())
//                        .questionOid(response.getQuestion().getOid())
//                     .userOid(response.getUser().getOid())
//                        .build()
    }

    //TODO tekrar eden veriler dönüyor.
    public List<AnswerResponseDto> findResponseByStudentTag(Long studentTagOid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("authentication failure.");
        }
        if ("anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("authentication failure.");
        }
        Long userOid = (Long) authentication.getCredentials();
        User user = userService.findActiveById(userOid).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

//        if (roleService.userHasRole(user, "ASSISTANT_TRAINER") || roleService.userHasRole(user, "MASTER_TRAINER")) {
//            Classroom classroom = classroomRepository.findActiveById(classroomOid).orElseThrow(() -> new ResourceNotFoundException("Classroom does not exist"));
//            if (!classroom.getUsers().contains(user)) {
//                throw new AccessDeniedException("authentication failure.");
//            }
//        }
//        Optional<Classroom> classroomOptional = classroomRepository.findActiveById(classroomOid);
//        if (classroomOptional.isEmpty()) {
//            throw new ResourceNotFoundException("Classroom is not found.");
//        }
        List<Survey> surveyList = surveyService.findAllActive();
        if (surveyList.isEmpty()) {
            throw new SurveyNotFoundException("There's a error while finding survey list");
        }
        List<Question> questionList = surveyList.stream().flatMap(s -> s.getQuestions().stream()
        ).toList();
        if (questionList.isEmpty()) {
            throw new QuestionNotFoundException("There's a error while finding questions");
        }
        List<Response> responseList = questionList.stream().flatMap(q -> q.getResponses().stream()).toList();
        if (responseList.isEmpty()) {
            throw new ResponseNotFoundException("There's a error while finding response");
        }
        List<AnswerResponseDto> answerResponseDtoList = new ArrayList<>();
        responseList.forEach(r -> answerResponseDtoList.add(AnswerResponseDto.builder()
                .responseString(r.getResponseString())
                .userOid(r.getUser().getOid())
                .questionOid(r.getQuestion().getOid())
                .surveyOid(r.getSurvey().getOid())
                .build()));
        return answerResponseDtoList;
    }

    public Boolean updateStudentAnswers(Long surveyOid, SurveyUpdateResponseRequestDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("authentication failure.");
        }
        if ("anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("authentication failure.");
        }
        Long userOid = (Long) authentication.getCredentials();
        userService.findActiveById(userOid).orElseThrow(() -> new UserDoesNotExistsException("User does not exist"));
        Survey survey = surveyService.findActiveById(surveyOid).orElseThrow(() -> new SurveyNotFoundException("There's a error while finding survey"));
        List<Response> responseList = survey.getQuestions().stream().flatMap(q -> q.getResponses().stream()).toList();
        if (responseList.isEmpty()) {
            throw new ResponseNotFoundException("There's a error while finding response");
        }
        responseList
                .stream()
                .filter(r -> r.getUser().getOid().equals(userOid))
                .forEach(response -> response.setResponseString(dto.getUpdateResponseMap().get(response.getOid())));
        responseRepository.saveAll(responseList);
        return true;
    }

    public Set<Response> findResponsesByUserOidAndSurveyOid(Long oid, Long surveyOid) {
        return responseRepository.findResponsesByUserOidAndSurveyOid(oid,surveyOid);
    }

    public void saveAll(List<Response> updatedResponses) {
        responseRepository.saveAll(updatedResponses);
    }

    public Set<Response> findBySurveyAndUser(Survey survey, User user) {
        return responseRepository.findBySurveyAndUser(survey,user);
    }
}
