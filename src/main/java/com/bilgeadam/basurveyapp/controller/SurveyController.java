package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.SurveyCreateRequestDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyResponseQuestionRequestDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyUpdateRequestDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyUpdateResponseRequestDto;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.services.SurveyService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<List<Survey>> getSurveyList() {
        return ResponseEntity.ok(surveyService.getSurveyList());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/page")
    ResponseEntity<Page<Survey>> getSurveyPage(Pageable pageable) {
        return ResponseEntity.ok(surveyService.getSurveyPage(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{surveyId}")
    ResponseEntity<Survey> findById(@PathVariable("surveyId") Long surveyId){
        return ResponseEntity.ok(surveyService.findByOid(surveyId));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    ResponseEntity<Survey> create(@RequestBody SurveyCreateRequestDto dto) {
        return ResponseEntity.ok(surveyService.create(dto));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping ("/update/{surveyId}")
    ResponseEntity<Survey> update(@PathVariable("surveyId") Long surveyId, @RequestBody SurveyUpdateRequestDto dto){
        return ResponseEntity.ok(surveyService.update(surveyId, dto));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping ("/delete/{surveyId}")
    ResponseEntity<Void> delete(@PathVariable("surveyId") Long surveyId){
        surveyService.delete(surveyId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/response/{surveyId}")
    @PreAuthorize("hasRole('STUDENT')")
    ResponseEntity<Survey> responseSurveyQuestions(@PathVariable("surveyId") Long surveyId, @RequestBody @Valid SurveyResponseQuestionRequestDto dto){
        return ResponseEntity.ok(surveyService.responseSurveyQuestions(surveyId,dto));
    }
    @PutMapping("/update-survey-response/{surveyId}")
    @PreAuthorize("hasRole('STUDENT')")
    ResponseEntity<Survey> updateSurveyAnswers(@PathVariable Long surveyId, @RequestBody @Valid SurveyUpdateResponseRequestDto dto){
        return ResponseEntity.ok(surveyService.updateSurveyAnswers(surveyId,dto));
    }
    @PutMapping("/{surveyId}/assign/{classroomId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    ResponseEntity<Survey> assignSurveyToClassroom(@PathVariable("surveyId") Long surveyId,@PathVariable("classroomId") Long classroomId){
        try {
            return ResponseEntity.ok(surveyService.assignSurveyToClassroom(surveyId,classroomId));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

