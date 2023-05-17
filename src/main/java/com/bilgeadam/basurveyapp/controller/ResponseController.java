package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.AnswerResponseDto;
import com.bilgeadam.basurveyapp.services.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/response")
@RequiredArgsConstructor
public class ResponseController {
    private final ResponseService responseService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update")
    @Operation(summary = "id ile response bulup string girdisi ile yeni bir response oluşmasını sağlayan metot.")
    public ResponseEntity<Void> updateResponse(@RequestBody @Valid ResponseRequestDto dto) {
        responseService.updateResponse(dto);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findbyid")
    @Operation(summary = "id girilerek bulunan response'un görüntülenmesini sağlayan metot.")
    public ResponseEntity<AnswerResponseDto> findById(@ParameterObject @Valid FindByIdRequestDto dto) {
        return ResponseEntity.ok(responseService.findByIdResponse(dto.getOid()));

    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findall")
    @Operation(summary = "Tüm response'ların görntülenmesini sağlayan metot.")
    public ResponseEntity<List<AnswerResponseDto>> findAll() {
        return ResponseEntity.ok(responseService.findAll());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete")
    @Operation(summary = "id ile bulunan response'un silinmesini sağlayan metot.")
    public ResponseEntity<Boolean> delete(@RequestParam @Valid Long responseOid) {
        return ResponseEntity.ok(responseService.deleteResponseById(responseOid));
    }

    @PutMapping("/savesurveyanswers/{token}")
    @Operation(summary = "id ile bulunan question'a verilen tüm response'ları kaydetmeye yarayan metot. #15")
    public ResponseEntity<Boolean> saveAll(@PathVariable @Valid String token,@RequestBody @Valid List<ResponseRequestSaveDto> responseRequestSaveDtoList){
        return ResponseEntity.ok(responseService.saveAll(token, responseRequestSaveDtoList));
    }


    @GetMapping("/findAllResponsesOfUserFromSurvey")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "survey id ve user email girilerek bulunan tüm response'ların görüntülenmesini sağlayan metot. #24")
    public ResponseEntity<List<AnswerResponseDto>> findAllResponsesOfUserFromSurvey(FindAllResponsesOfUserFromSurveyRequestDto dto) {
        return ResponseEntity.ok(responseService.findAllResponsesOfUserFromSurvey(dto));
    }

    @GetMapping("/findAllResponsesOfUser")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "survey id ve user email girilerek bulunan tüm response'ların görüntülenmesini sağlayan metot. #24")
    public ResponseEntity<List<AnswerResponseDto>> findAllResponsesOfUser(String userEmail) {
        return ResponseEntity.ok(responseService.findAllResponsesOfUser(userEmail));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'MASTER_TRAINER', 'ASISTANT_TRAINER')")
    @GetMapping("/findResponseByStudentTag")
    @Operation(summary = "clasroom id'si girilerek bulunan tüm response'ların görüntülenmesini sağlayan metot.")
    public ResponseEntity<List<AnswerResponseDto>> findResponseByStudentTag(@RequestParam Long studentTagOid) {
        return ResponseEntity.ok(responseService.findResponseByStudentTag(studentTagOid));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STUDENT')")
    @PostMapping("/updateStudentResponses")
    @Operation(summary = "id ile bulunan response için string türünde girdi ile yeni değer atanmasını sağlayan metot.  #23")
    public ResponseEntity<Boolean> updateStudentResponses(@RequestParam Long surveyOid, @RequestBody SurveyUpdateResponseRequestDto dto) {
        return ResponseEntity.ok(responseService.updateStudentAnswers(surveyOid, dto));
    }
}
