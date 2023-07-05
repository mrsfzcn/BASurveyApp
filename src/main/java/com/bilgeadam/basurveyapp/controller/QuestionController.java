package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.*;
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
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;



    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    @Operation(summary = "String türünde bir soru, long türünde soru tipi ve integer türünde order girilerek yeni soru oluşturulmasını sağlayan metot. #10")
    public ResponseEntity<Boolean> createQuestion(@RequestBody @Valid List<CreateQuestionDto> createQuestionDtoList) {

        return ResponseEntity.ok(questionService.createQuestions(createQuestionDtoList));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update")
    @Operation(summary = "Oid ile mevcut bir soruyu bulup string türünde yeni bir sorunun aynı id ile yazılmasını sağlayan metot.")
    public ResponseEntity<Boolean> updateQuestion(@RequestBody @Valid UpdateQuestionDto updateQuestionDto) {
        return ResponseEntity.ok(questionService.updateQuestion(updateQuestionDto));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findbyid/{id}")
    @Operation(summary = "Integer türünde id girilerek o id'ye denk gelen sorunun görntülenmesini sağlayan metot.")
    public ResponseEntity<QuestionFindByIdResponseDto> findById(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(questionService.findById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findall")
    @Operation(summary = "Tüm soruların görntülenmesini sağlayan metot.")
    public ResponseEntity<List<QuestionResponseDto>> findAll() {
        List<QuestionResponseDto> responseDtoList = questionService.findAll();
        return ResponseEntity.ok(responseDtoList);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete")
    @Operation(summary = "Ihnteger türünde id girilerek bulunan sorunun silinmesini sağlayan metot.")
    public ResponseEntity<Boolean> delete(@RequestBody @Valid Long questionId) {
        return ResponseEntity.ok(questionService.delete(questionId));
    }

    @GetMapping("/getsurveyquestions/{token}")
    @Operation(summary = "Token kullanılarak anket sorularına ulaşılmasını sağlayan metot. #14")
    public ResponseEntity<List<QuestionResponseDto>> getSurveyQuestions(@PathVariable String token) {
        return ResponseEntity.ok(questionService.findAllSurveyQuestions(token));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/filterbykeyword")
    @Operation(summary = "Id ile survey'e ulaşıp string tütünde anahtar kelimenin içinde geçtiği soruları görüntülemeyi sağlayan metot.")
    public ResponseEntity<List<QuestionResponseDto>> filterSurveyQuestionsByKeyword(@RequestBody @Valid FilterSurveyQuestionsByKeywordRequestDto dto) {
        return ResponseEntity.ok(questionService.filterSurveyQuestionsByKeyword(dto));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/filtersurveyquestions")
    @Operation(summary = "Id ile survey bulup, survey'deki tüm soruları görntülemeyi sağlayan metot.")
    public ResponseEntity<List<QuestionResponseDto>> filterSurveyQuestions(@RequestBody @Valid FilterSurveyQuestionsRequestDto dto) {
        return ResponseEntity.ok(questionService.filterSurveyQuestions(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ASSISTANT_TRAINER', 'MASTER_TRAINER')")
    @PostMapping("/questionsbytrainertype")
    @Operation(summary = "Id ile trainer'ı ve survey'i bulup tüm soruları görüntülemeyi sağlayan metot.")
    ResponseEntity<List<QuestionsTrainerTypeResponseDto>> QuestionsByTrainerType(@RequestBody GetQuestionByRoleIdRequestDto dto) {

        return ResponseEntity.ok(questionService.questionByTrainerType(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findallbyquestiontype/{questionType}")
    @Operation(summary = "belirli bir question type'a sahip tüm soruları görüntülemeyi sağlayan metot.")
    public ResponseEntity<List<String>> findAllByQuestionType(@PathVariable @Valid String questionType){
        return ResponseEntity.ok(questionService.findAllByQuestionType(questionType));
    }

}
