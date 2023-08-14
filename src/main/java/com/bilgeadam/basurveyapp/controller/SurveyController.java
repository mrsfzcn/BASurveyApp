package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.services.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/get-all-survey-question-response-by-student")
    @Operation(summary = "Bir anketteki belirli bir student tag' e sahip olan ogrencilerin -her soru için ayrı şekilde- verdigi yanıtları donduren methot #Oğuz")
    ResponseEntity<List<SurveyQuestionResponseByStudentResponseDto>> getAllSurveyQuestionResponseByStudent(SurveyQuestionResponseByStudentRequestDto dto){
        return ResponseEntity.ok(surveyService.getAllSurveyQuestionResponseByStudent(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/list")
    @Operation(summary = "Tüm survey'leri görüntülemeyi sağlayan metot. #16")
    ResponseEntity<List<SurveySimpleResponseDto>> getSurveyList() {
        return ResponseEntity.ok(surveyService.getSurveyList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/page")
    ResponseEntity<Page<Survey>> getSurveyPage(Pageable pageable) {
        return ResponseEntity.ok(surveyService.getSurveyPage(pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-survey-by-id/{survey-id}")
    @Operation(summary = "Survey id'si girilerek ulaşılan survey'in görüntülebnmesini sağlayan metot. #17")
    ResponseEntity<SurveyResponseDto> findByOid(@PathVariable("survey-id") Long surveyId) {
        return ResponseEntity.ok(surveyService.findByOid(surveyId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    @Operation(summary = "String türünde title ve topic girilerek yeni bir survey oluşturulmasını sağlayan metot. #11")
    ResponseEntity<SurveySimpleResponseDto> create(@RequestBody @Valid SurveyCreateRequestDto dto) {
        return ResponseEntity.ok(surveyService.create(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/assign-survey-tag")
    @Operation(summary = "Long olarak surveyTag0id ve survey0id girilir. Birden fazla tag için ',' ile ayrılmalıdır.")
    ResponseEntity<SurveySimpleResponseDto> assignSurveyTag(@RequestBody @Valid SurveyTagAssignRequestDto dto) {
        return ResponseEntity.ok(surveyService.assignSurveyTag(dto));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/add-question-to-survey")
    @Operation(summary = "survey ve question id'si girilere bir survey'e yeni bir soru eklemeyi sağlayan metot. #12")
    ResponseEntity<Boolean> addQuestionToSurvey(@RequestBody SurveyAddQuestionRequestDto dto) {
        return ResponseEntity.ok(surveyService.addQuestionToSurvey(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/add-questions-to-survey")
    @Operation(summary = "survey id'si ve eklenecek soruların id'leri eklenerek yeni soru eklemeyi sağlayan metot. #12")
    ResponseEntity<Boolean> addQuestionsToSurvey( @RequestBody @Valid SurveyAddQuestionsRequestDto dto) {
        surveyService.addQuestionsToSurvey( dto);
        return ResponseEntity.ok(true);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update")
    @Operation(summary = "String türünde survey title girilerek ulaşılan suver'de değişiklik yapılmasını sağlayan metot.")
    ResponseEntity<Survey> update(@RequestBody @Valid SurveyUpdateRequestDto dto) {
        return ResponseEntity.ok(surveyService.update(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete/{survey-id}")
    @Operation(summary = "Suver id girilerek ulaşılan survey'in silinmesini sağlayan metot.")
    ResponseEntity<Void> delete(@PathVariable("survey-id") Long surveyId) {
        surveyService.delete(surveyId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "anketi ögrenciye atama işlemi#13")
    ResponseEntity<Boolean> assignSurveyToClassroom(@RequestBody SurveyAssignRequestDto surveyAssignRequestDto) throws MessagingException {
        return ResponseEntity.ok(surveyService.assignSurveyToClassroom(surveyAssignRequestDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-surveys-by-student-tag")
    @Operation(summary = "#19")
    public ResponseEntity<List<SurveyByStudentTagResponseDto>> findSurveysByStudentTag(
            @RequestParam Long studentTagOid) {
        return ResponseEntity.ok(surveyService.findSurveysByStudentTag(studentTagOid));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','STUDENT')")
    @GetMapping("/find-surveys-by-student-oid")
    @Operation(summary = "#18")
    ResponseEntity<List<SurveyByStudentTagResponseDto>> findSurveysByStudentOid(@RequestParam Long studentOid) {
        return ResponseEntity.ok(surveyService.findSurveysByStudentOid(studentOid));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-survey-answers-unmasked")
    @Operation(summary = "#20")
    ResponseEntity<SurveyResponseWithAnswersDto> findSurveyAnswersUnmasked(@ParameterObject FindSurveyAnswersRequestDto dto) {
        return ResponseEntity.ok(surveyService.findSurveyAnswersUnmasked(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-masked-survey-answers-as-admin-or-manager")
    @Operation(summary = "#21")
    ResponseEntity<SurveyOfClassroomMaskedResponseDto> findMaskedSurveyAnswersAsAdminOrManager(@ParameterObject FindSurveyAnswersRequestDto findSurveyAnswersRequestDto) {
        return ResponseEntity.ok(surveyService.findMaskedSurveyAnswersAsAdminOrManager(findSurveyAnswersRequestDto));
    }

    @PreAuthorize("hasAnyRole('MASTER_TRAINER', 'ASSISTANT_TRAINER')")
    @GetMapping("/find-masked-survey-answers-as-trainer")
    @Operation(summary = "#22")
    ResponseEntity<SurveyOfClassroomMaskedResponseDto> findMaskedSurveyAnswersAsTrainer(@ParameterObject FindSurveyAnswersRequestDto findSurveyAnswersRequestDto) {
        return ResponseEntity.ok(surveyService.findMaskedSurveyAnswersAsTrainer(findSurveyAnswersRequestDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','MASTER_TRAINER', 'ASSISTANT_TRAINER')")
    @GetMapping("/find-trainer-surveys")
    ResponseEntity<TrainerClassroomSurveyResponseDto> findTrainerSurveys() {
        return ResponseEntity.ok(surveyService.findTrainerSurveys());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','MASTER_TRAINER', 'ASSISTANT_TRAINER')")
    @GetMapping("/find-survey-participants")
    ResponseEntity<SurveyParticipantResponseDto> findSurveyParticipants(SurveyParticipantRequestDto dto) {
        return ResponseEntity.ok(surveyService.findSurveyParticipants(dto));
    }

}

