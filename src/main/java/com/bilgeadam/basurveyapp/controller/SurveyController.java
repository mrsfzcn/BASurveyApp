package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.services.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.List;


@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/list")
    @Operation(summary = "Tüm survey'leri görüntülemeyi sağlayan metot.")
    ResponseEntity<List<SurveyResponseDto>> getSurveyList() {
        return ResponseEntity.ok(surveyService.getSurveyList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/page")
    @Operation(summary = "")
    ResponseEntity<Page<Survey>> getSurveyPage(Pageable pageable) {
        return ResponseEntity.ok(surveyService.getSurveyPage(pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{surveyId}")
    @Operation(summary = "Survey id'si girilerek ulaşılan survey'in görüntülebnmesini sağlayan metot.")
    ResponseEntity<SurveySimpleResponseDto> findById(@PathVariable("surveyId") Long surveyId) {
        return ResponseEntity.ok(surveyService.findByOid(surveyId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    @Operation(summary = "String türünde title ve topic girilerek yeni bir survey oluşturulmasını sağlayan metot.")
    ResponseEntity<Boolean> create(@RequestBody @Valid SurveyCreateRequestDto dto) {
        return ResponseEntity.ok(surveyService.create(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/add-question-to-survey")
    @Operation(summary = "survey ve question id'si girilere bir survey'e yeni bir soru eklemeyi sağlayan metot.")
    ResponseEntity<Boolean> addQuestionToSurvey( @RequestBody @Valid SurveyAddQuestionRequestDto dto) {
        return ResponseEntity.ok(surveyService.addQuestionToSurvey( dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/add-questions-to-survey")
    @Operation(summary = "survey id'si ve eklenecek soruların id'leri eklenerek yeni soru eklemeyi sağlayan metot.")
    ResponseEntity<Boolean> addQuestionsToSurvey( @RequestBody @Valid SurveyAddQuestionsRequestDto dto) {
        surveyService.addQuestionsToSurvey( dto);
        return ResponseEntity.ok(true);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update/{surveyId}")
    @Operation(summary = "String türünde survey title girilerek ulaşılan suver'de değişiklik yapılmasını sağlayan metot.")
    ResponseEntity<Survey> update(@PathVariable("surveyId") Long surveyId, @RequestBody SurveyUpdateRequestDto dto) {
        return ResponseEntity.ok(surveyService.update(surveyId, dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete/{surveyId}")
    @Operation(summary = "Suver id girilerek ulaşılan survey'in silinmesini sağlayan metot.")
    ResponseEntity<Void> delete(@PathVariable("surveyId") Long surveyId) {
        surveyService.delete(surveyId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-survey-response/{surveyId}")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Survey id girilerek response'ların değiştirilebilmesini sağlayan metot.")
    ResponseEntity<Survey> updateSurveyResponses(@PathVariable Long surveyId, @RequestBody @Valid SurveyUpdateResponseRequestDto dto) {
        return ResponseEntity.ok(surveyService.updateSurveyResponses(surveyId, dto));
    }

    @PutMapping("/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "")
    ResponseEntity<Boolean> assignSurveyToClassroom(@RequestBody SurveyAssignRequestDto surveyAssignRequestDto) throws MessagingException {
        return ResponseEntity.ok(surveyService.assignSurveyToClassroom(surveyAssignRequestDto));
    }

    @PostMapping("/response/{token}")
    @Operation(summary = "")
    ResponseEntity<Boolean> responseSurveyQuestions(@PathVariable("token") String token, @RequestBody @Valid List<SurveyResponseQuestionRequestDto> dtoList, HttpServletRequest request) {
        return ResponseEntity.ok(surveyService.responseSurveyQuestions(token, dtoList, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','MASTER_TRAINER', 'ASSISTANT_TRAINER')")
    @GetMapping("/findSurveyByClassroomOid")
    @Operation(summary = "")
    ResponseEntity<List<SurveyByStudentTagResponseDto>> findSurveyByClassroomOid(@RequestParam Long classroomOid) {
        return ResponseEntity.ok(surveyService.findByStudentTagOid(classroomOid));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','STUDENT')")
    @GetMapping("/findSurveyByStudentOid")
    @Operation(summary = "")
    ResponseEntity<List<SurveyByStudentTagResponseDto>> findSurveyByStudentOid() {
        return ResponseEntity.ok(surveyService.findStudentSurveys());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findsurveyanswersunmasked")
    @Operation(summary = "")
    ResponseEntity<SurveyResponseWithAnswersDto> findSurveyAnswersUnmasked(@ParameterObject FindSurveyAnswersRequestDto dto) {
        return ResponseEntity.ok(surveyService.findSurveyAnswersUnmasked(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findMaskedSurveyAnswersAsAdminOrManager")
    @Operation(summary = "")
    ResponseEntity<SurveyOfClassroomMaskedResponseDto> findMaskedSurveyAnswersAsAdminOrManager(@ParameterObject FindSurveyAnswersRequestDto findSurveyAnswersRequestDto) {
        return ResponseEntity.ok(surveyService.findMaskedSurveyAnswersAsAdminOrManager(findSurveyAnswersRequestDto));
    }

    @PreAuthorize("hasAnyRole('MASTER_TRAINER', 'ASSISTANT_TRAINER')")
    @GetMapping("/findMaskedSurveyAnswersAsTrainer")
    @Operation(summary = "")
    ResponseEntity<SurveyOfClassroomMaskedResponseDto> findMaskedSurveyAnswersAsTrainer(@ParameterObject FindSurveyAnswersRequestDto findSurveyAnswersRequestDto) {
        return ResponseEntity.ok(surveyService.findMaskedSurveyAnswersAsTrainer(findSurveyAnswersRequestDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','MASTER_TRAINER', 'ASSISTANT_TRAINER')")
    @GetMapping("/trainersurveys")
    @Operation(summary = "")
    ResponseEntity<TrainerClassroomSurveyResponseDto> findTrainerSurveys() {
        return ResponseEntity.ok(surveyService.findTrainerSurveys());
    }

}

