package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateTagDto;
import com.bilgeadam.basurveyapp.dto.response.TagResponseDto;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTagNotFoundException;
import com.bilgeadam.basurveyapp.services.QuestionTagService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question-tags")
@RequiredArgsConstructor
public class QuestionTagController {

    private final QuestionTagService questionTagService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    @Operation(summary = "String türünde tag ismi girilerek yeni question tag oluşturulmasını sağlayan metot. #9")
    @Hidden
    public ResponseEntity<QuestionTag> createTag(@RequestBody @Valid CreateTagDto dto) {
        QuestionTag createdTag = questionTagService.createTag(dto);
        return ResponseEntity.ok(createdTag);
    }

    @PutMapping("/update-tag-by-tag-string/{tag-string}")
    @Operation(summary = "Belirtilen tag stringine sahip olan question tag'in güncellenmesini sağlayan metot")
    @Hidden
    public ResponseEntity<QuestionTag> updateTagByTagString(
            @PathVariable("tag-string") String tagString,
            @RequestBody @Valid UpdateTagDto dto) {
        try {
            QuestionTag questionTag = questionTagService.updateTagByTagString(tagString,dto.getNewTagString());
            return ResponseEntity.ok(questionTag);
        } catch (QuestionTagNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping
    @Operation(summary = "Tüm tag'lerin görüntülenmesini sağlayan metot.")
    public ResponseEntity<List<TagResponseDto>> findAll() {
        List<TagResponseDto> responseDtoList = questionTagService.findAll();
        return ResponseEntity.ok(responseDtoList);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete-by-tag-string")
    @Operation(summary = "Tag stringine göre bulunan question tag'in silinmesini sağlayan metot.")
    public ResponseEntity<Boolean> deleteByTagString(@RequestParam @Valid String tagString) {
        System.out.println("*******-> "+tagString);
        try {
            boolean deleted = questionTagService.deleteByTagString(tagString);
            return ResponseEntity.ok(deleted);
        } catch (QuestionTagNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
