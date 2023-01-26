package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateQuestionTypeRequestDto;
import com.bilgeadam.basurveyapp.dto.request.FindByIdRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateQuestionTypeRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AllQuestionTypeResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionTypeFindByIdResponseDto;
import com.bilgeadam.basurveyapp.services.QuestionTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questiontype")
@RequiredArgsConstructor
public class QuestionTypeController {
    private final QuestionTypeService questionTypeService;

    @GetMapping("/test")
    public String test() {
        return "questiontype";
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createQuestionType(@RequestBody @Valid CreateQuestionTypeRequestDto dto) {
        questionTypeService.createQuestionType(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateQuestionType(@RequestBody @Valid UpdateQuestionTypeRequestDto dto) {
        questionTypeService.updateQuestionType(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/findbyid")
    public ResponseEntity<QuestionTypeFindByIdResponseDto> findById(@RequestBody @Valid FindByIdRequestDto dto) {
        return ResponseEntity.ok(questionTypeService.findById(dto.getOid()));

    }

    @PostMapping("/findall")
    public ResponseEntity<List<AllQuestionTypeResponseDto>> findAllQuestionTypeList() {
        List<AllQuestionTypeResponseDto> responseDtoList = questionTypeService.findAll();
        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody @Valid Long questionTypeId) {
        return ResponseEntity.ok(questionTypeService.delete(questionTypeId));
    }


}
