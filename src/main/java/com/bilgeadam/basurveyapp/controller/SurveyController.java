package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.*;
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
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
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
    ResponseEntity<SurveySimpleResponseDto> findById(@PathVariable("surveyId") Long surveyId) {
        return ResponseEntity.ok(surveyService.findByOid(surveyId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    ResponseEntity<Boolean> create(@RequestBody SurveyCreateRequestDto dto) {
        return ResponseEntity.ok(surveyService.create(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/add-question-to-survey")
    ResponseEntity<Boolean> addQuestionToSurvey( @RequestBody @Valid SurveyAddQuestionRequestDto dto) {
        return ResponseEntity.ok(surveyService.addQuestionToSurvey( dto));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/add-questions-to-survey")
    ResponseEntity<Boolean> addQuestionsToSurvey( @RequestBody @Valid SurveyAddQuestionsRequestDto dto) {
        surveyService.addQuestionsToSurvey( dto);
        return ResponseEntity.ok(true);
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
    ResponseEntity<Boolean> assignSurveyToClassroom(@RequestBody SurveyAssignRequestDto surveyAssignRequestDto) throws MessagingException, ParseException {
        return ResponseEntity.ok(surveyService.assignSurveyToClassroom(surveyAssignRequestDto));
    }

    @PostMapping("/response/{token}")
    ResponseEntity<Boolean> responseSurveyQuestions(@PathVariable("token") String token, @RequestBody @Valid List<SurveyResponseQuestionRequestDto> dtoList, HttpServletRequest request) {
        return ResponseEntity.ok(surveyService.responseSurveyQuestions(token, dtoList, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','MASTER_TRAINER', 'ASSISTANT_TRAINER')")
    @GetMapping("/findSurveyByClassroomOid")
    ResponseEntity<List<SurveyByStudentTagResponseDto>> findSurveyByClassroomOid(@RequestParam Long classroomOid) {
        return ResponseEntity.ok(surveyService.findByStudentTagOid(classroomOid));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','STUDENT')")
    @GetMapping("/findSurveyByStudentOid")
    ResponseEntity<List<SurveyByStudentTagResponseDto>> findSurveyByStudentOid() {
        return ResponseEntity.ok(surveyService.findStudentSurveys());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','MASTER_TRAINER', 'ASSISTANT_TRAINER')")
    @GetMapping("/findSurveyAnswers")
    ResponseEntity<SurveyOfClassroomMaskedResponseDto> findSurveyAnswers(@ParameterObject FindSurveyAnswersRequestDto findSurveyAnswersRequestDto) {
        return ResponseEntity.ok(surveyService.findSurveyAnswers(findSurveyAnswersRequestDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','MASTER_TRAINER', 'ASSISTANT_TRAINER')")
    @GetMapping("/trainersurveys")
    ResponseEntity<TrainerClassroomSurveyResponseDto> findTrainerSurveys() {
        return ResponseEntity.ok(surveyService.findTrainerSurveys());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findsurveyanswersunmasked")
    ResponseEntity<SurveyResponseWithAnswersDto> findSurveyAnswersUnmasked(@ParameterObject FindSurveyAnswersRequestDto dto){
        return ResponseEntity.ok(surveyService.findSurveyAnswersUnmasked(dto));
    }

}

