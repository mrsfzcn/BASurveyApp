package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.FindSurveyAnswersRequestDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyAssignRequestDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyCreateRequestDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyResponseQuestionRequestDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyUpdateRequestDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyUpdateResponseRequestDto;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.services.SurveyService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    @GetMapping("/test")
    public String test() {
        return "survey";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/list")
    ResponseEntity<List<SurveyResponseDto>> getSurveyList() {
        return ResponseEntity.ok(surveyService.getSurveyList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/page")
    ResponseEntity<Page<Survey>> getSurveyPage(Pageable pageable) {
        return ResponseEntity.ok(surveyService.getSurveyPage(pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{surveyId}")
    ResponseEntity<Survey> findById(@PathVariable("surveyId") Long surveyId) {
        return ResponseEntity.ok(surveyService.findByOid(surveyId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    ResponseEntity<Boolean> create(@RequestBody SurveyCreateRequestDto dto) {
        return ResponseEntity.ok(surveyService.create(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update/{surveyId}")
    ResponseEntity<Survey> update(@PathVariable("surveyId") Long surveyId, @RequestBody SurveyUpdateRequestDto dto) {
        return ResponseEntity.ok(surveyService.update(surveyId, dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete/{surveyId}")
    ResponseEntity<Void> delete(@PathVariable("surveyId") Long surveyId) {
        surveyService.delete(surveyId);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/update-survey-response/{surveyId}")
    @PreAuthorize("hasRole('STUDENT')")
    ResponseEntity<Survey> updateSurveyResponses(@PathVariable Long surveyId, @RequestBody @Valid SurveyUpdateResponseRequestDto dto) {
        return ResponseEntity.ok(surveyService.updateSurveyResponses(surveyId, dto));
    }

    @PutMapping("/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    ResponseEntity<Boolean> assignSurveyToClassroom(@RequestBody SurveyAssignRequestDto surveyAssignRequestDto) throws MessagingException {
        return ResponseEntity.ok(surveyService.assignSurveyToClassroom(surveyAssignRequestDto));
    }

    @PostMapping("/response/{token}")
    ResponseEntity<Boolean> responseSurveyQuestions(@PathVariable("token") String token, @RequestBody @Valid SurveyResponseQuestionRequestDto dto, HttpServletRequest request) {
        return ResponseEntity.ok(surveyService.responseSurveyQuestions(token, dto, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','MASTER_TRAINER', 'ASSISTANT_TRAINER')")
    @GetMapping("/findSurveyByClassroomOid")
    ResponseEntity<List<SurveyByClassroomResponseDto>> findSurveyByClassroomOid(@RequestParam Long classroomOid) {
        return ResponseEntity.ok(surveyService.findByClassroomOid(classroomOid));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','MASTER_TRAINER', 'ASSISTANT_TRAINER')")
    @GetMapping("/findSurveyAnswers")
    ResponseEntity<SurveyOfClassroomMaskedResponseDto> findSurveyAnswers(@ParameterObject FindSurveyAnswersRequestDto findSurveyAnswersRequestDto) {
        return ResponseEntity.ok(surveyService.findSurveyAnswers(findSurveyAnswersRequestDto));
    }

    @PreAuthorize("hasAnyRole('MASTER_TRAINER', 'ASSISTANT_TRAINER')")
    @GetMapping("/trainersurveys")
    ResponseEntity<TrainerClassroomSurveyResponseDto> findTrainerSurveys() {
        return ResponseEntity.ok(surveyService.findTrainerSurveys());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findsurveyanswersunmasked")
    ResponseEntity<SurveyOfClassroomResponseDto> findSurveyAnswersUnmasked(@ParameterObject FindSurveyAnswersRequestDto dto){
        return ResponseEntity.ok(surveyService.findSurveyAnswersUnmasked(dto));
    }

}

