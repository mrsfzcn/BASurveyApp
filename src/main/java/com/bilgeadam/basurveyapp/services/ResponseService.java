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
        User user = getAuthenticatedUser();
        Question question = getActiveQuestionById(responseRequestDto.getQuestionOid());

        Response response = buildResponse(responseRequestDto, question, user);
        responseRepository.save(response);
    }

    public void updateResponse(ResponseRequestDto responseRequestDto) {
        Response response = responseRepository.findActiveById(responseRequestDto.getResponseOid())
                .orElseThrow(() -> new ResponseNotFoundException("There's an error while finding response"));

        response.setResponseString(responseRequestDto.getResponseString());
        responseRepository.save(response);
    }

    public AnswerResponseDto findByIdResponse(Long responseOid) {
        Response response = responseRepository.findActiveById(responseOid)
                .orElseThrow(() -> new ResponseNotFoundException("There's an error while finding response"));
        return ResponseMapper.INSTANCE.toAnswerResponseDto(response);
    }

    public List<AnswerResponseDto> findAll() {
        ResponseMapper responseMapper = ResponseMapper.INSTANCE;
        List<Response> findAllList = responseRepository.findAllActive();
        return findAllList.stream()
                .map(responseMapper::toAnswerResponseDto).collect(Collectors.toList());
    }

    public Boolean deleteResponseById(Long responseOid) {
        Response response = responseRepository.findActiveById(responseOid)
                .orElseThrow(() -> new ResponseNotFoundException("There's an error while finding response"));
        return responseRepository.softDeleteById(response.getOid());
    }

    public Boolean saveAll(String token, List<ResponseRequestSaveDto> responseRequestSaveDtoList) {
        User user = getUserByEmailFromToken(token);
        Survey survey = getActiveSurveyByIdFromToken(token);
        if (responseRepository.isSurveyAnsweredByUser(user.getOid(), survey.getOid())) {
            return false;
        }
        Map<Long, Question> questionMap = new HashMap<>();
        responseRequestSaveDtoList.forEach(responseDto -> {
            Question question = questionMap.computeIfAbsent(responseDto.getQuestionOid(), this::getActiveQuestionById);
            Response response = createResponse(responseDto, question, survey, user);
            updateStudentAndSurvey(response, user, survey);
            saveResponse(response, question, survey);
        });
        return true;
    }

    public List<AnswerResponseDto> findAllResponsesOfUserFromSurvey(FindAllResponsesOfUserRequestDto dto) {
        ResponseMapper responseMapper = ResponseMapper.INSTANCE;
        userService.findByEmail(dto.getUserEmail()).orElseThrow(() -> new UserDoesNotExistsException("User does not exists or deleted."));
        Survey survey = surveyService.findActiveById(dto.getSurveyOid()).orElseThrow(() -> new ResourceNotFoundException("Survey does not exists or deleted."));
        return responseRepository
                .findAllResponsesOfUserFromSurvey(dto.getUserEmail(), questionService.findSurveyQuestionOidList(survey.getOid()))
                .stream()
                .map(responseMapper::toAnswerResponseDto).collect(Collectors.toList());
    }

    public List<AnswerResponseDto> findResponseByStudentTag(Long studentTagOid) {
        User user = getAuthenticatedUser();

        List<Response> responseList = getAllResponsesFromActiveSurveys();
        if (responseList.isEmpty()) {
            throw new ResponseNotFoundException("No responses found.");
        }

        return responseList.stream()
                .distinct()
                .map(this::mapToAnswerResponseDto)
                .toList();
    }

    public Boolean updateStudentAnswers(Long surveyOid, SurveyUpdateResponseRequestDto dto) {
        User user = getAuthenticatedUser();
        Survey survey = getActiveSurveyById(surveyOid);
        List<Response> responseList = getAllResponsesForSurvey(survey);
        if (responseList.isEmpty()) {
            throw new ResponseNotFoundException("No responses found for the survey.");
        }

        responseList.stream()
                .filter(r -> r.getUser().getOid().equals(user.getOid()))
                .forEach(response -> response.setResponseString(dto.getUpdateResponseMap().get(response.getOid())));
        responseRepository.saveAll(responseList);
        return true;
    }
    private Response buildResponse(ResponseRequestSaveDto responseRequestDto, Question question, User user) {
        return Response.builder()
                .responseString(responseRequestDto.getResponseString())
                .question(question)
                .user(user)
                .build();
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Authentication failure.");
        }
        if ("anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("Authentication failure.");
        }
        Long userOid = (Long) authentication.getCredentials();
        return userService.findActiveById(userOid)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist."));
    }

    private List<Response> getAllResponsesFromActiveSurveys() {
        List<Survey> surveyList = surveyService.findAllActive();
        if (surveyList.isEmpty()) {
            throw new SurveyNotFoundException("No active surveys found.");
        }
        return surveyList.stream()
                .flatMap(s -> s.getQuestions().stream())
                .flatMap(q -> q.getResponses().stream())
                .collect(Collectors.toList());
    }

    private AnswerResponseDto mapToAnswerResponseDto(Response response) {
        return AnswerResponseDto.builder()
                .responseString(response.getResponseString())
                .userOid(response.getUser().getOid())
                .questionOid(response.getQuestion().getOid())
                .surveyOid(response.getSurvey().getOid())
                .build();
    }

    private Survey getActiveSurveyById(Long surveyId) {
        return surveyService.findActiveById(surveyId)
                .orElseThrow(() -> new SurveyNotFoundException("Survey not found."));
    }

    private List<Response> getAllResponsesForSurvey(Survey survey) {
        return survey.getQuestions().stream()
                .flatMap(q -> q.getResponses().stream())
                .collect(Collectors.toList());
    }

    private User getUserByEmailFromToken(String token) {
        String userEmail = jwtService.extractEmail(token);
        return userService.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("No such user."));
    }

    private Survey getActiveSurveyByIdFromToken(String token) {
        Long surveyId = jwtService.extractSurveyOid(token);
        return surveyService.findActiveById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("No such survey."));
    }

    private Question getActiveQuestionById(Long questionId) {
        return questionService.findActiveById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("Question not found"));
    }

    private Response createResponse(ResponseRequestSaveDto responseDto, Question question, Survey survey, User user) {
        return Response.builder()
                .responseString(responseDto.getResponseString())
                .question(question)
                .survey(survey)
                .user(user)
                .build();
    }

    private void updateStudentAndSurvey(Response response, User user, Survey survey) {
        Optional<Student> student = studentService.findByUser(user);
        student.get().getSurveysAnswered().add(survey);
        survey.getStudentsWhoAnswered().add(student.get());
        studentService.save(student.get());
    }

    private void saveResponse(Response response, Question question, Survey survey) {
        responseRepository.save(response);
        question.getResponses().add(response);
        questionService.save(question);
        survey.getResponses().add(response);
        surveyService.save(survey);
    }

}
