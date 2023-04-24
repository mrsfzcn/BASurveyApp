package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.dto.request.FindAllResponsesOfUserRequestDto;
import com.bilgeadam.basurveyapp.dto.request.ResponseRequestDto;
import com.bilgeadam.basurveyapp.dto.request.ResponseRequestSaveDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyUpdateResponseRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AnswerResponseDto;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.Response;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.SurveyNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.UserDoesNotExistsException;
import com.bilgeadam.basurveyapp.mapper.ResponseMapper;
import com.bilgeadam.basurveyapp.repositories.QuestionRepository;
import com.bilgeadam.basurveyapp.repositories.ResponseRepository;
import com.bilgeadam.basurveyapp.repositories.SurveyRepository;
import com.bilgeadam.basurveyapp.repositories.UserRepository;
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
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final SurveyRepository surveyRepository;
//    private final ClassroomRepository classroomRepository;
    private final RoleService roleService;

    public void createResponse(ResponseRequestSaveDto responseRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("authentication failure.");
        }
        if ("anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("authentication failure.");
        }
        Long userOid = (Long) authentication.getCredentials();
        userRepository.findActiveById(userOid).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

        Response response = ResponseMapper.INSTANCE.toResponse(responseRequestDto, questionRepository
                .findActiveById(responseRequestDto
                        .getQuestionOid()).orElseThrow(() -> new ResourceNotFoundException("question not found")), userRepository.findActiveById(userOid).orElseThrow(() -> new ResourceNotFoundException("User does not exist")));
        responseRepository.save(response);
    }

    public void updateResponse(ResponseRequestDto responseRequestDto) {
        Optional<Response> updatedResponse = responseRepository.findActiveById(responseRequestDto.getResponseOid());
        if (updatedResponse.isEmpty()) {
            throw new ResourceNotFoundException("There's a error while finding response");
        } else {
            updatedResponse.get().setResponseString(responseRequestDto.getResponseString());
            responseRepository.save(updatedResponse.get());
        }
    }

    public AnswerResponseDto findByIdResponse(Long responseOid) {
        Optional<Response> response = responseRepository.findActiveById(responseOid);
        if (response.isEmpty()) {
            throw new ResourceNotFoundException("There's a error while finding response");
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
        List<AnswerResponseDto> responseDtoList = findAllList.stream()
                .map(responseMapper::toAnswerResponseDto).collect(Collectors.toList());
//        List<AnswerResponseDto> responseDtoList = new ArrayList<>();
//        findAllList.forEach(response ->
//                responseDtoList.add(AnswerResponseDto.builder()
//                        .responseString(response.getResponseString())
//                        .userOid(response.getUser().getOid())
//                        .questionOid(response.getQuestion().getOid())
//                        .build()));
        return responseDtoList;
    }

    public Boolean deleteResponseById(Long responseOid) {
        Optional<Response> response = responseRepository.findActiveById(responseOid);
        if (response.isEmpty()) {
            throw new ResourceNotFoundException("There's a error while finding response");
        } else {
           return responseRepository.softDeleteById(response.get().getOid());
        }
    }

    public Boolean saveAll(String token, List<ResponseRequestSaveDto> responseRequestSaveDtoList) {
        User user = userRepository.findByEmail(jwtService.extractEmail(token)).orElseThrow(() -> new ResourceNotFoundException("No such user."));// tokendan gelen id var gibi kabul edildi.Mail üzerinden yapıdı.
        Survey survey = surveyRepository.findActiveById(jwtService.extractSurveyOid(token)).orElseThrow(() -> new ResourceNotFoundException("No such survey."));
        if (!responseRepository.isSurveyAnsweredByUser(user.getOid(), survey.getOid())) {
            Map<Long, Question> questionMap = new HashMap<>();
            responseRequestSaveDtoList.forEach(response -> {
                Question question = questionMap.computeIfAbsent(response.getQuestionOid(),
                        id -> questionRepository.findActiveById(id)
                                .orElseThrow(() -> new QuestionNotFoundException("Question not found")));
                Response newResponse = Response.builder()
                        .responseString(response.getResponseString())
                        .question(question)
                        .survey(surveyRepository.findActiveById(survey.getOid())
                                .orElseThrow(() -> new SurveyNotFoundException("Survey not found")))
                        .user(user)
                        .build();
                responseRepository.save(newResponse);
                question.getResponses().add(newResponse);
                questionRepository.save(question);
                survey.getResponses().add(newResponse);
                surveyRepository.save(survey);
            });
            return true;
        } else {
            return false;
        }
    }

    public List<AnswerResponseDto> findAllResponsesOfUserFromSurvey(FindAllResponsesOfUserRequestDto dto) {
        ResponseMapper responseMapper = ResponseMapper.INSTANCE;
        userRepository.findByEmail(dto.getUserEmail()).orElseThrow(() -> new UserDoesNotExistsException("User does not exists or deleted."));
        Survey survey = surveyRepository.findActiveById(dto.getSurveyOid()).orElseThrow(() -> new ResourceNotFoundException("Survey does not exists or deleted."));
        return responseRepository
                .findAllResponsesOfUserFromSurvey(dto.getUserEmail(), questionRepository.findSurveyQuestionOidList(survey.getOid()))
                .stream()
                .map(responseMapper::toAnswerResponseDto).collect(Collectors.toList());
//                        -> AnswerResponseDto.builder()
//                        .responseString(response.getResponseString())
//                        .questionOid(response.getQuestion().getOid())
//                     .userOid(response.getUser().getOid())
//                        .build()
    }

    public List<AnswerResponseDto> findResponseByClassroomOid(Long classroomOid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("authentication failure.");
        }
        if ("anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("authentication failure.");
        }
        Long userOid = (Long) authentication.getCredentials();
        User user = userRepository.findActiveById(userOid).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

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
        List<Survey> surveyList = surveyRepository.findAllActive();
        if (surveyList.isEmpty()) {
            throw new ResourceNotFoundException("There's a error while finding survey list");
        }
        List<Question> questionList = surveyList.stream().flatMap(s -> s.getQuestions().stream()
        ).toList();
        if (questionList.isEmpty()) {
            throw new ResourceNotFoundException("There's a error while finding questions");
        }
        List<Response> responseList = questionList.stream().flatMap(q -> q.getResponses().stream()).toList();
        if (responseList.isEmpty()) {
            throw new ResourceNotFoundException("There's a error while finding response");
        }
        List<AnswerResponseDto> answerResponseDtoList = new ArrayList<>();
        responseList.forEach(r -> answerResponseDtoList.add(AnswerResponseDto.builder()
                .responseString(r.getResponseString())
                .userOid(r.getUser().getOid())
                .questionOid(r.getQuestion().getOid())
                .build()));
        return answerResponseDtoList;
    }

    public Boolean updateStudentAnswers(Long surveyOid, SurveyUpdateResponseRequestDto dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("authentication failure.");
        }
        if ("anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("authentication failure.");
        }
        Long userOid = (Long) authentication.getCredentials();
        userRepository.findActiveById(userOid).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
        Survey survey = surveyRepository.findActiveById(surveyOid).orElseThrow(()->new ResourceNotFoundException("There's a error while finding survey"));
        List<Response>responseList=survey.getQuestions().stream().flatMap(q->q.getResponses().stream()).toList();
        if (responseList.isEmpty()) {
            throw new ResourceNotFoundException("There's a error while finding response");
        }
        responseList
                .stream()
                .filter(r->r.getUser().getOid().equals(userOid))
                .forEach(response -> response.setResponseString(dto.getUpdateResponseMap().get(response.getOid())));
        responseRepository.saveAll(responseList);
        return true;
    }
}
