package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.SurveyCreateRequestDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyResponseQuestionRequestDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyUpdateRequestDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyUpdateResponseRequestDto;
import com.bilgeadam.basurveyapp.entity.Classroom;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.Response;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
import com.bilgeadam.basurveyapp.repositories.ClassroomRepository;
import com.bilgeadam.basurveyapp.repositories.ResponseRepository;
import com.bilgeadam.basurveyapp.repositories.SurveyRepository;
import com.bilgeadam.basurveyapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final ClassroomRepository classroomRepository;
    private final ResponseRepository responseRepository;
    private final UserRepository userRepository;

    public List<Survey> getSurveyList() {
        return new ArrayList<>(surveyRepository.findAllActive());
    }

    public Page<Survey> getSurveyPage(Pageable pageable) {
        return surveyRepository.findAllActive(pageable);
    }

    public Survey create(SurveyCreateRequestDto dto) {
        Optional<Classroom> classroomOptional = classroomRepository.findById(dto.getClassroomId());
        if (classroomOptional.isEmpty()) {
            // TODO specific exception
            throw new RuntimeException("Classroom is not found");
        }
        Survey survey = Survey.builder()
                .surveyTitle(dto.getSurveyTitle())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .classroom(classroomOptional.get())
                .questions(dto.getQuestions())
                .courseTopic(dto.getCourseTopic())
                .build();
        return surveyRepository.save(survey);
    }

    public Survey update(Long surveyId, SurveyUpdateRequestDto dto) {

        Optional<Survey> surveyToBeUpdated = surveyRepository.findActiveById(surveyId);
        if (surveyToBeUpdated.isEmpty()) {
            // TODO specific exception
            throw new RuntimeException("Classroom is not found");
        }
        surveyToBeUpdated.get().setSurveyTitle(dto.getSurveyTitle());
        return surveyRepository.save(surveyToBeUpdated.get());
    }

    public void delete(Long surveyId) {

        Optional<Survey> surveyToBeDeleted = surveyRepository.findActiveById(surveyId);
        if (surveyToBeDeleted.isEmpty()) {
            // TODO specific exception
            throw new RuntimeException("Classroom is not found");
        }
        surveyRepository.softDelete(surveyToBeDeleted.get());
    }

    public Survey findByOid(Long surveyId) {

        Optional<Survey> surveyById = surveyRepository.findActiveById(surveyId);
        if (surveyById.isEmpty()) {
            // TODO specific exception
            throw new RuntimeException("Classroom is not found");
        }
        return surveyById.get();
    }

    public Survey responseSurveyQuestions(Long surveyId, SurveyResponseQuestionRequestDto dto) {

        // todo test yapılmadı
        Optional<Survey> surveyOptional = surveyRepository.findActiveById(surveyId);
        if(surveyOptional.isEmpty()){
            throw new ResourceNotFoundException("Survey is not Found.");
        }
        Optional<Long> currentUserIdOptional= Optional.of((Long) SecurityContextHolder.getContext().getAuthentication().getCredentials());
        Long currentUserId = currentUserIdOptional.orElseThrow(() -> new ResourceNotFoundException("Token does not contain User Info"));
        Optional<User> currentUserOptional = userRepository.findActiveById(currentUserId);
        if (currentUserOptional.isEmpty()){
            throw new ResourceNotFoundException("User is not found");
        }
        List<Question> surveyQuestions = surveyOptional.get().getQuestions();
        List<Response> responses = dto.getResponses().keySet()
            .parallelStream()
            .map((id -> Response.builder()
                .responseString(dto.getResponses().get(id))
                .question(surveyQuestions
                    .stream()
                    .filter(question -> question.getOid().equals(id))
                    .findAny()
                    .orElse(null))
                .user(currentUserOptional.get())
                .build()))
            .toList();
        for(Response response : responses){
            Optional<Question> questionOptional = surveyQuestions
                .parallelStream()
                .filter(question -> question.getOid().equals(response.getQuestion().getOid()))
                .findAny();
            if(questionOptional.isPresent()){
                questionOptional.get().getResponses().add(response);
            }
        }
        return surveyRepository.save(surveyOptional.get());
    }
    public Survey updateSurveyAnswers(Long surveyId, SurveyUpdateResponseRequestDto dto) {

        // todo test yapılmadı
        Optional<Survey> surveyOptional = surveyRepository.findActiveById(surveyId);
        if(surveyOptional.isEmpty()){
            throw new ResourceNotFoundException("Survey is not Found.");
        }
        if(surveyOptional.get().getEndDate().before(new Date())){
            throw new ResourceNotFoundException("Survey is Expired.");
        }
        Optional<Long> currentUserIdOptional= Optional.of((Long) SecurityContextHolder.getContext().getAuthentication().getCredentials());
        Long currentUserId = currentUserIdOptional.orElseThrow(() -> new ResourceNotFoundException("Token does not contain User Info"));
        List<Response> currentUserResponses = surveyOptional.get().getQuestions()
            .stream()
            .flatMap(question -> question.getResponses().stream())
            .filter(response -> response.getOid().equals(currentUserId))
            .collect(Collectors.toList());
        currentUserResponses
            .parallelStream()
            .filter(response -> dto.getUpdateAnswerMap().containsKey(response.getOid()))
            .forEach(response -> response.setResponseString(dto.getUpdateAnswerMap().get(response.getOid())));
        responseRepository.saveAll(currentUserResponses);
        return surveyOptional.get();
    }

}