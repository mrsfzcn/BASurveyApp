package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.AnswerResponseDto;
import com.bilgeadam.basurveyapp.dto.response.WhoDidntAnswerSurveyStudentDto;
import com.bilgeadam.basurveyapp.entity.Response;
import com.bilgeadam.basurveyapp.services.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/responses")
@RequiredArgsConstructor
public class ResponseController {
    private final ResponseService responseService;
    @PostMapping
    @Operation(summary = "Response Create etmek için kullanilan methot")
    public ResponseEntity<Boolean> createResponse (@RequestBody @Valid ResponseRequestSaveDto responseRequestSaveDto){
        responseService.createResponse(responseRequestSaveDto);
        return ResponseEntity.ok(true);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update-response/{id}")
    @Operation(summary = "id ile response bulup string girdisi ile yeni bir response oluşmasını sağlayan metot.")
    public ResponseEntity<Void> updateResponse(@PathVariable Long id,@RequestBody @Valid ResponseRequestDto dto) {
        responseService.updateResponse(dto,id);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-by-id/{id}")
    @Operation(summary = "id girilerek bulunan response'un görüntülenmesini sağlayan metot.")
    public ResponseEntity<AnswerResponseDto> findById(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(responseService.findById(id));

    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping
    @Operation(summary = "Tüm response'ların görntülenmesini sağlayan metot.")
    public ResponseEntity<List<AnswerResponseDto>> findAll() {
        return ResponseEntity.ok(responseService.findAll());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete-response-by-id/{id}")
    @Operation(summary = "id ile bulunan response'un silinmesini sağlayan metot.")
    public ResponseEntity<Boolean> deleteResponseById(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(responseService.deleteResponseById(id));
    }

    @PutMapping("/survey-answers/{token}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "id ile bulunan question'a verilen tüm response'ları kaydetmeye yarayan metot. #15")
    public ResponseEntity<Boolean> saveAll(@PathVariable @Valid String token,@RequestBody @Valid List<ResponseRequestSaveDto> responseRequestSaveDtoList){
        return ResponseEntity.ok(responseService.saveAll(token, responseRequestSaveDtoList));
    }


    @GetMapping("/find-all-responses-of-user-from-survey")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "survey id ve user email girilerek bulunan tüm response'ların görüntülenmesini sağlayan metot. #24")
    public ResponseEntity<List<AnswerResponseDto>> findAllResponsesOfUserFromSurvey(FindAllResponsesOfUserFromSurveyRequestDto dto) {
        return ResponseEntity.ok(responseService.findAllResponsesOfUserFromSurvey(dto));
    }

    @GetMapping("/find-all-responses-of-user")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "survey id ve user email girilerek bulunan tüm response'ların görüntülenmesini sağlayan metot. #24")
    public ResponseEntity<List<AnswerResponseDto>> findAllResponsesOfUser(String userEmail) {
        return ResponseEntity.ok(responseService.findAllResponsesOfUser(userEmail));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'MASTER_TRAINER', 'ASISTANT_TRAINER')")
    @GetMapping("/find-response-by-student-tag")
    @Operation(summary = "clasroom id'si girilerek bulunan tüm response'ların görüntülenmesini sağlayan metot.")
    public ResponseEntity<List<AnswerResponseDto>> findResponseByStudentTag(@RequestParam Long studentTagOid) {
        return ResponseEntity.ok(responseService.findResponseByStudentTag(studentTagOid));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STUDENT')")
    @PostMapping("/update-student-responses")
    @Operation(summary = "id ile bulunan response için string türünde girdi ile yeni değer atanmasını sağlayan metot.  #23")
    public ResponseEntity<Boolean> updateStudentAnswers(@RequestParam Long surveyOid, @RequestBody SurveyUpdateResponseRequestDto dto) {
        return ResponseEntity.ok(responseService.updateStudentAnswers(surveyOid, dto));
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/excel/{id}")
    @Operation(summary = "anket cevaplarını excele export eden metod")
    public ResponseEntity<byte[]> exportToExcel(@PathVariable Long id) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment","result.xlsx");
        return new ResponseEntity<>(responseService.exportToExcel(id),headers, HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/survey-response-rate/{survey-id}/{student-tag-oid}")
    @Operation(summary = "anketin doldurulma oranını döndüren metod")
    public ResponseEntity<Double> surveyResponseRate(@PathVariable(name = "survey-id")Long surveyId, @PathVariable(name = "student-tag-oid") Long studentTagOid){
        return ResponseEntity.ok(responseService.surveyResponseRate(surveyId,studentTagOid));
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/survey-response-rates/{survey-id}/{student-tag-oid}")
    @Operation(summary = "anketin atandığı sınıftaki öğrencilerin ad soyadını döndüren metod")
    public ResponseEntity<List<String>> surveyResponseRateName(@PathVariable(name = "survey-id")Long surveyId, @PathVariable(name = "student-tag-oid") Long studentTagOid){
        return ResponseEntity.ok(responseService.surveyResponseRateName(surveyId,studentTagOid));
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/who-need-to-complete/{survey-id}")
    @Operation(summary = "anketi cevaplaması gereken öğrencileri bulan metod ")
    public ResponseEntity<List<WhoDidntAnswerSurveyStudentDto>> whoNeedToComplete(@PathVariable(name = "survey-id") Long surveyId){
        return ResponseEntity.ok(responseService.whoNeedToComplete(surveyId));
    }

}
