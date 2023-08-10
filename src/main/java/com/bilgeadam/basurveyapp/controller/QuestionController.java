package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.FindAllQuestionResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionFindByIdResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionsTrainerTypeResponseDto;
import com.bilgeadam.basurveyapp.services.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;



    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("")
    @Operation(summary = "String türünde bir soru, long türünde soru tipi ve integer türünde order girilerek yeni soru oluşturulmasını sağlayan metot. #10")
    public ResponseEntity<Boolean> createQuestion(@RequestBody @Valid List<CreateQuestionDto> createQuestionDtoList) {

        return ResponseEntity.ok(questionService.createQuestion(createQuestionDtoList));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update-question")
    @Operation(summary = "Oid ile mevcut bir soruyu bulup string türünde yeni bir sorunun aynı id ile yazılmasını sağlayan metot.")
    public ResponseEntity<Boolean> updateQuestion(@RequestBody @Valid UpdateQuestionDto updateQuestionDto) {
        return ResponseEntity.ok(questionService.updateQuestion(updateQuestionDto));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-by-id/{id}")
    @Operation(summary = "Integer türünde id girilerek o id'ye denk gelen sorunun görntülenmesini sağlayan metot.")
    public ResponseEntity<QuestionFindByIdResponseDto> findById(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(questionService.findById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-all")
    @Operation(summary = "Tüm soruların görntülenmesini sağlayan metot.")
    public ResponseEntity<List<QuestionResponseDto>> findAll() {
        List<QuestionResponseDto> responseDtoList = questionService.findAll();
        return ResponseEntity.ok(responseDtoList);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-all-question")
    @Operation(summary = "Tüm soruların görntülenmesini sağlayan metot.")
    public ResponseEntity<List<FindAllQuestionResponseDto>> findAllQuestion() {
        List<FindAllQuestionResponseDto> responseDtoList = questionService.findAllQuestion();
        return ResponseEntity.ok(responseDtoList);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete-question-by-id/{id}")
    @Operation(summary = "Integer türünde id girilerek bulunan sorunun silinmesini sağlayan metot.")
    public ResponseEntity<Boolean> deleteQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.delete(id));
    }

    @GetMapping("/survey-questions/{student-token}")
    @Operation(summary = "Token kullanılarak anket sorularına ulaşılmasını sağlayan metot. #14")
    public ResponseEntity<List<QuestionResponseDto>> getSurveyQuestions(@PathVariable(name = "student-token") String token) {
        return ResponseEntity.ok(questionService.findAllSurveyQuestions(token));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{survey-oid}/keyword")
    @Operation(summary = "Id ile survey'e ulaşıp string tütünde anahtar kelimenin içinde geçtiği soruları görüntülemeyi sağlayan metot.")
    public ResponseEntity<List<QuestionResponseDto>> filterSurveyQuestionsByKeyword(@PathVariable("survey-oid") Long survey0id,@RequestParam String keyword) {
        return ResponseEntity.ok(questionService.filterSurveyQuestionsByKeyword(survey0id,keyword));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/survey-oid/question-tag")
    @Operation(summary = "Id ile survey bulup, survey'deki tüm soruları görntülemeyi sağlayan metot.")
    public ResponseEntity<List<QuestionResponseDto>> filterSurveyQuestions(@RequestParam Long survey0id, @RequestParam List<Long> questionTag0ids) {
        return ResponseEntity.ok(questionService.filterSurveyQuestions(survey0id,questionTag0ids));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ASSISTANT_TRAINER', 'MASTER_TRAINER')")
    @GetMapping("/trainer-id/survey-id")
    @Operation(summary = "Id ile trainer'ı ve survey'i bulup tüm soruları görüntülemeyi sağlayan metot.")
    ResponseEntity<List<QuestionsTrainerTypeResponseDto>> QuestionsByTrainerType(@RequestParam Long trainerid,@RequestParam Long surveyid) {
        return ResponseEntity.ok(questionService.questionByTrainerType(trainerid,surveyid));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/type")
    @Operation(summary = "belirli bir question type'a sahip tüm soruları görüntülemeyi sağlayan metot.")
    public ResponseEntity<List<String>> findAllByQuestionType(@RequestParam String type){
        return ResponseEntity.ok(questionService.findAllByQuestionType(type));
    }

}
