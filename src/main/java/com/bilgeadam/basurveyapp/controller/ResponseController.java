package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.FindByIdRequestDto;
import com.bilgeadam.basurveyapp.dto.request.ResponseRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AllQuestionResponseDto;
import com.bilgeadam.basurveyapp.dto.response.AnswerResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionTypeFindByIdResponseDto;
import com.bilgeadam.basurveyapp.services.ResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/response")
@RequiredArgsConstructor
public class ResponseController {
    private final ResponseService responseService;
    @PostMapping("/create")
    public ResponseEntity<Void> createResponse(@RequestBody @Valid ResponseRequestDto dto, @Valid Long userOid) {
        responseService.createResponse(dto, userOid);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateResponse(@RequestBody @Valid ResponseRequestDto dto, @Valid Long userOid) {
        responseService.updateResponse(dto, userOid);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/findbyid")
    public ResponseEntity<AnswerResponseDto> findById(@RequestBody @Valid FindByIdRequestDto dto) {
        return ResponseEntity.ok(responseService.findByIdResponse(dto.getOid()));

    }
    @GetMapping("/findall")
    public ResponseEntity<List<AnswerResponseDto>> findAll() {
        return ResponseEntity.ok(responseService.findAll());
    }

    @PostMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestParam @Valid Long responseOid) {
        return ResponseEntity.ok(responseService.deleteResponseById(responseOid));
    }
}
