package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.response.TagResponseDto;
import com.bilgeadam.basurveyapp.services.SurveyTagService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/surveytag")
@RequiredArgsConstructor
public class SurveyTagController {

    private final SurveyTagService surveyTagService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    @Operation(summary = "#04")
    public ResponseEntity<String> createTag(@RequestBody @Valid CreateTagDto dto ){
        surveyTagService.createTag(dto);
        return ResponseEntity.ok(dto.getTagString());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findall")
    public ResponseEntity<List<TagResponseDto>> findAllTag() {
        List<TagResponseDto> responseDtoList = surveyTagService.findAll();
        return ResponseEntity.ok(responseDtoList);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody @Valid Long tagStringId) {
        return ResponseEntity.ok(surveyTagService.delete(tagStringId));
    }
}
