package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.*;
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


@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/getallsurveyquestionresponsebystudent")
    @Operation(summary = "Bir anketteki belirli bir student tag' e sahip olan ogrencilerin -her soru için ayrı şekilde- verdigi yanıtları donduren method #Oğuz")
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
    @GetMapping("/findSurveyByid/{surveyId}")
    @Operation(summary = "Survey id'si girilerek ulaşılan survey'in görüntülebnmesini sağlayan metot. #17")
    ResponseEntity<SurveyResponseDto> findById(@PathVariable("surveyId") Long surveyId) {
        return ResponseEntity.ok(surveyService.findByOid(surveyId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    @Operation(summary = "String türünde title ve topic girilerek yeni bir survey oluşturulmasını sağlayan metot. #11")
    ResponseEntity<Boolean> create(@RequestBody @Valid SurveyCreateRequestDto dto) {
        return ResponseEntity.ok(surveyService.create(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/assignSurveyTag")
    @Operation(summary = "String türünde title ve topic girilerek yeni bir survey oluşturulmasını sağlayan metot. #11")
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
/* /response/updateStudentResponses response controller a taşınmış
    @PutMapping("/update-survey-response/{surveyId}")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Survey id girilerek response'ların değiştirilebilmesini sağlayan metot.")
    ResponseEntity<Survey> updateSurveyResponses(@PathVariable Long surveyId, @RequestBody @Valid SurveyUpdateResponseRequestDto dto) {
        return ResponseEntity.ok(surveyService.updateSurveyResponses(surveyId, dto));
    }
*/

    @PutMapping("/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "#13")
    ResponseEntity<Boolean> assignSurveyToClassroom(@RequestBody SurveyAssignRequestDto surveyAssignRequestDto) throws MessagingException {
        return ResponseEntity.ok(surveyService.assignSurveyToClassroom(surveyAssignRequestDto));
    }
/* /savesurveyanswers/{token} response controller a taşınmış
    @PostMapping("/response/{token}")
    @Operation(summary = "")
    ResponseEntity<Boolean> responseSurveyQuestions(@PathVariable("token") String token, @RequestBody @Valid List<SurveyResponseQuestionRequestDto> dtoList, HttpServletRequest request) {
        return ResponseEntity.ok(surveyService.responseSurveyQuestions(token, dtoList, request));
    }
*/
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findSurveysByStudentTag")
    @Operation(summary = "#19")
    public ResponseEntity<List<SurveyByStudentTagResponseDto>> findSurveysByStudentTag(
            @RequestParam Long studentTagOid) {
        return ResponseEntity.ok(surveyService.findSurveysByStudentTag(studentTagOid));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','STUDENT')")
    @GetMapping("/findSurveysByStudentOid")
    @Operation(summary = "#18")
    ResponseEntity<List<SurveyByStudentTagResponseDto>> findSurveysByStudentOid(@RequestParam Long studentOid) {
        return ResponseEntity.ok(surveyService.findSurveysByStudentOid(studentOid));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findsurveyanswersunmasked")
    @Operation(summary = "#20")
    ResponseEntity<SurveyResponseWithAnswersDto> findSurveyAnswersUnmasked(@ParameterObject FindSurveyAnswersRequestDto dto) {
        return ResponseEntity.ok(surveyService.findSurveyAnswersUnmasked(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findMaskedSurveyAnswersAsAdminOrManager")
    @Operation(summary = "#21")
    ResponseEntity<SurveyOfClassroomMaskedResponseDto> findMaskedSurveyAnswersAsAdminOrManager(@ParameterObject FindSurveyAnswersRequestDto findSurveyAnswersRequestDto) {
        return ResponseEntity.ok(surveyService.findMaskedSurveyAnswersAsAdminOrManager(findSurveyAnswersRequestDto));
    }

    @PreAuthorize("hasAnyRole('MASTER_TRAINER', 'ASSISTANT_TRAINER')")
    @GetMapping("/findMaskedSurveyAnswersAsTrainer")
    @Operation(summary = "#22")
    ResponseEntity<SurveyOfClassroomMaskedResponseDto> findMaskedSurveyAnswersAsTrainer(@ParameterObject FindSurveyAnswersRequestDto findSurveyAnswersRequestDto) {
        return ResponseEntity.ok(surveyService.findMaskedSurveyAnswersAsTrainer(findSurveyAnswersRequestDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','MASTER_TRAINER', 'ASSISTANT_TRAINER')")
    @GetMapping("/trainersurveys")
    ResponseEntity<TrainerClassroomSurveyResponseDto> findTrainerSurveys() {
        return ResponseEntity.ok(surveyService.findTrainerSurveys());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','MASTER_TRAINER', 'ASSISTANT_TRAINER')")
    @GetMapping("/findSurveyParticipants")
    ResponseEntity<SurveyParticipantResponseDto> findSurveyParticipants(SurveyParticipantRequestDto dto) {
        return ResponseEntity.ok(surveyService.findSurveyParticipants(dto));
    }

}

