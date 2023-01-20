package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateQuestionTypeRequestDto;
import com.bilgeadam.basurveyapp.dto.request.QuestionTypeFindByIdRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateQuestionTypeRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AllQuestionTypeResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionTypeFindByIdResponseDto;
import com.bilgeadam.basurveyapp.services.QuestionService;
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
    public ResponseEntity<Void> createQuestionType(@RequestBody @Valid CreateQuestionTypeRequestDto dto, @Valid Long userOid) {
        questionTypeService.createQuestionType(dto, userOid);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateQuestionType(@RequestBody @Valid UpdateQuestionTypeRequestDto dto, @Valid Long userOid) {
        questionTypeService.updateQuestionType(dto, userOid);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/findbyid")
    public ResponseEntity<QuestionTypeFindByIdResponseDto> findById(@RequestBody @Valid QuestionTypeFindByIdRequestDto dto) {
        return ResponseEntity.ok(questionTypeService.findById(dto.getQuestionTypeId()));

    }

    @PostMapping("/findall")
    public ResponseEntity<List<AllQuestionTypeResponseDto>> findAllQuestionTypeList() {
        List<AllQuestionTypeResponseDto> responseDtoList = questionTypeService.findAll();
        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody @Valid Long questionTypeId, @Valid Long userOid) {
        return ResponseEntity.ok(questionTypeService.delete(questionTypeId, userOid));
    }


}
