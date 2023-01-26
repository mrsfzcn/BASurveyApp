package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.SurveyCreateRequestDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyUpdateRequestDto;
import com.bilgeadam.basurveyapp.entity.Classroom;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.repositories.IClassroomRepository;
import com.bilgeadam.basurveyapp.repositories.ISurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final ISurveyRepository surveyRepository;
    private final IClassroomRepository classroomRepository;
    public List<Survey> getSurveyList() {

        return new ArrayList<>(surveyRepository.findAll());
    }

    public Page<Survey> getSurveyPage(Pageable pageable) {

        return surveyRepository.findAll(pageable);
    }

    public Survey create(SurveyCreateRequestDto dto) throws Exception {
        Optional<Classroom> classroomOptional = classroomRepository.findById(dto.getClassroomId());
        if(classroomOptional.isEmpty()){
            throw new Exception("Clasroom is not found");
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
        if(surveyToBeUpdated.isEmpty()){

        }
        surveyToBeUpdated.get().setSurveyTitle(dto.getSurveyTitle());
        return surveyRepository.save(surveyToBeUpdated.get());
    }
    public void delete(Long surveyId) {

        Optional<Survey> surveyToBeDeleted = surveyRepository.findActiveById(surveyId);
        if(surveyToBeDeleted.isEmpty()){

        }
        surveyRepository.softDelete(surveyToBeDeleted.get());
    }

    public Survey findByOid(Long surveyId) {

        Optional<Survey> surveyById = surveyRepository.findActiveById(surveyId);
        if(surveyById.isEmpty()){

        }
        return surveyById.get();
    }

}