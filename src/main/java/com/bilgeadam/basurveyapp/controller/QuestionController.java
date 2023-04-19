package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.QuestionFindByIdResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionsTrainerTypeResponseDto;
import com.bilgeadam.basurveyapp.services.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/test")
    public String test() {
        return "question";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<Boolean> createQuestion(@RequestBody @Valid CreateQuestionDto createQuestionDto) {

        return ResponseEntity.ok(questionService.createQuestion(createQuestionDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update")
    public ResponseEntity<Boolean> updateQuestion(@RequestBody @Valid UpdateQuestionDto updateQuestionDto) {
        return ResponseEntity.ok(questionService.updateQuestion(updateQuestionDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/findbyid")
    public ResponseEntity<QuestionFindByIdResponseDto> findById(@RequestBody @Valid FindByIdRequestDto findByIdRequestDto) {
        return ResponseEntity.ok(questionService.findById(findByIdRequestDto.getOid()));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findall")
    public ResponseEntity<List<QuestionResponseDto>> findAll() {
        List<QuestionResponseDto> responseDtoList = questionService.findAll();
        return ResponseEntity.ok(responseDtoList);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody @Valid Long questionId) {
        return ResponseEntity.ok(questionService.delete(questionId));
    }

    @GetMapping("/getsurveyquestions/{token}")
    public ResponseEntity<List<QuestionResponseDto>> getSurveyQuestions(@PathVariable String token) {
        return ResponseEntity.ok(questionService.findAllSurveyQuestions(token));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/filterbykeyword")
    public ResponseEntity<List<QuestionResponseDto>> filterSurveyQuestionsByKeyword(@RequestBody @Valid FilterSurveyQuestionsByKeywordRequestDto dto) {
        return ResponseEntity.ok(questionService.filterSurveyQuestionsByKeyword(dto));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/filtersurveyquestions")
    public ResponseEntity<List<QuestionResponseDto>> filterSurveyQuestions(@RequestBody @Valid FilterSurveyQuestionsRequestDto dto) {
        return ResponseEntity.ok(questionService.filterSurveyQuestions(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ASSISTANT_TRAINER', 'MASTER_TRAINER')")
    @PostMapping("/questionsbytrainertype")
    ResponseEntity<List<QuestionsTrainerTypeResponseDto>> QuestionsByTrainerType(@RequestBody GetQuestionByRoleIdRequestDto dto) {

        return ResponseEntity.ok(questionService.questionByTrainerType(dto));
    }

}
