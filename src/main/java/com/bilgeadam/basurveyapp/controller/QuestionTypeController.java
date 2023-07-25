package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateQuestionTypeRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateQuestionTypeRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AllQuestionTypeResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionTypeFindByIdResponseDto;
import com.bilgeadam.basurveyapp.services.QuestionTypeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questiontypes")
@RequiredArgsConstructor
public class QuestionTypeController {
    private final QuestionTypeService questionTypeService;



    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    @Operation(summary = "String türünde questiontype ismi girilerek yeni bir tür oluşturan metot. #8")
    public ResponseEntity<Boolean> createQuestionType(@RequestBody @Valid CreateQuestionTypeRequestDto dto) {
        return ResponseEntity.ok(questionTypeService.createQuestionType(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/updateQuestionType/{id}")
    @Operation(summary = "Id ile questiontype'a ulaşılıp string türünde yeni questiontype girilmesini sağlayan metot.")
    public ResponseEntity<Void> updateQuestionType(@RequestBody @Valid UpdateQuestionTypeRequestDto dto,@PathVariable Long id) {
        questionTypeService.updateQuestionType(dto,id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findById/{id}")
    @Operation(summary = "Id ile questiontype'a ulaşıp görüntülenmesini sağlayan metot.")
    public ResponseEntity<QuestionTypeFindByIdResponseDto> findById(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(questionTypeService.findById(id));

    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping
    @Operation(summary = "Tüm question type'ları görüntülemeyi sağlayan metot.")
    public ResponseEntity<List<AllQuestionTypeResponseDto>> findAll() {
        List<AllQuestionTypeResponseDto> responseDtoList = questionTypeService.findAll();
        return ResponseEntity.ok(responseDtoList);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("delete/{id}")
    @Operation(summary = "id numarası ile ulaşılan question type'ı silmeyi sağlayan metot.")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return ResponseEntity.ok(questionTypeService.delete(id));
    }


}
