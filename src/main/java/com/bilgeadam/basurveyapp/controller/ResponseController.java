package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.FindAllResponsesOfUserRequestDto;
import com.bilgeadam.basurveyapp.dto.request.FindByIdRequestDto;
import com.bilgeadam.basurveyapp.dto.request.ResponseRequestDto;
import com.bilgeadam.basurveyapp.dto.request.ResponseRequestSaveDto;
import com.bilgeadam.basurveyapp.dto.response.AnswerResponseDto;
import com.bilgeadam.basurveyapp.services.ResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/response")
@RequiredArgsConstructor
public class ResponseController {
    private final ResponseService responseService;
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'MASTER_TRAINER', 'ASSISTANT_TRAINER', 'STUDENT')")
    @PostMapping("/create")
    public ResponseEntity<Void> createResponse(@RequestBody @Valid ResponseRequestSaveDto dto) {
        responseService.createResponse(dto);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/update")
    public ResponseEntity<Void> updateResponse(@RequestBody @Valid ResponseRequestDto dto) {
        responseService.updateResponse(dto);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findbyid")
    public ResponseEntity<AnswerResponseDto> findById(@RequestBody @Valid FindByIdRequestDto dto) {
        return ResponseEntity.ok(responseService.findByIdResponse(dto.getOid()));

    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findall")
    public ResponseEntity<List<AnswerResponseDto>> findAll() {
        return ResponseEntity.ok(responseService.findAll());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping
    public ResponseEntity<Boolean> delete(@RequestParam @Valid Long responseOid) {
        return ResponseEntity.ok(responseService.deleteResponseById(responseOid));
    }
    @PutMapping("/savesurveyanswers/{token}")
    public ResponseEntity<Boolean> saveAll(@PathVariable @Valid String token,@RequestBody @Valid List<ResponseRequestSaveDto> responseRequestSaveDtoList){
        return ResponseEntity.ok(responseService.saveAll(token, responseRequestSaveDtoList));//tokendan hangi survey ve user olduğunun tespit edip response dtodaki response entitye çevirir.
    }

    @GetMapping("/findallresponsesofuserfromsurvey")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<AnswerResponseDto>> findAllResponsesOfUserFromSurvey(FindAllResponsesOfUserRequestDto dto) {
        return ResponseEntity.ok(responseService.findAllResponsesOfUserFromSurvey(dto));
    }
// Bu methodu düzelteceğim -yusuf
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'MASTER_TRAINER', 'ASISTANT_TRAINER')")
//    @GetMapping("/findResponseByClassroomOid")
//    public ResponseEntity<List<AnswerResponseDto>>findResponseByClassroomOid(@RequestParam Long classroomOid){
//        return ResponseEntity.ok(responseService.findResponseByClassroomOid(classroomOid));
//    }
}
