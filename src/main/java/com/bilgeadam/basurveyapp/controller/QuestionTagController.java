package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.response.TagResponseDto;
import com.bilgeadam.basurveyapp.services.QuestionTagService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questiontag")
@RequiredArgsConstructor
public class QuestionTagController {

    private final QuestionTagService questionTagService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    @Operation(summary = "String türünde tag ismi girilerek yeni question tag oluşturulmasını sağlayan metot. #9")
    public ResponseEntity<String> createTag(@RequestBody @Valid CreateTagDto dto ){
        questionTagService.createTag(dto);
        return ResponseEntity.ok(dto.getTagString());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findall")
    @Operation(summary = "Tüm tag'lerin görüntülenmesini sağlayan metot.")
    public ResponseEntity<List<TagResponseDto>> findAllTag() {
        List<TagResponseDto> responseDtoList = questionTagService.findAll();
        return ResponseEntity.ok(responseDtoList);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete")
    @Operation(summary = "id numarası ile bulunan question tag'in silinmesini sağlayan metot.")
    public ResponseEntity<Boolean> delete(@RequestBody @Valid Long tagStringId) {
        return ResponseEntity.ok(questionTagService.delete(tagStringId));
    }
}
