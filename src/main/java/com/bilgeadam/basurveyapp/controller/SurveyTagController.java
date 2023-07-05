package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateTagDto;
import com.bilgeadam.basurveyapp.dto.response.TagResponseDto;
import com.bilgeadam.basurveyapp.entity.tags.SurveyTag;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTagNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.SurveyTagNotFoundException;
import com.bilgeadam.basurveyapp.services.SurveyTagService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<SurveyTag> createTag(@RequestBody @Valid CreateTagDto dto ){
        SurveyTag createdTag = surveyTagService.createTag(dto);
        return ResponseEntity.ok(createdTag);
    }

    @PutMapping("/updatebytagstring/{tagString}")
    @Operation(summary = "Belirtilen tag stringine sahip olan survey tag'in güncellenmesini sağlayan metot")
    public ResponseEntity<SurveyTag> updateTagByTagString(
            @PathVariable("tagString") String tagString,
            @RequestBody @Valid UpdateTagDto dto) {
        try {
            SurveyTag surveyTag = surveyTagService.updateTagByTagString(tagString, dto);
            return ResponseEntity.ok(surveyTag);
        } catch (SurveyTagNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findall")
    public ResponseEntity<List<TagResponseDto>> findAllTag() {
        List<TagResponseDto> responseDtoList = surveyTagService.findAll();
        return ResponseEntity.ok(responseDtoList);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete/{tagString}")
    @Operation(summary = "Tag stringine göre bulunan question tag'in silinmesini sağlayan metot.")
    public ResponseEntity<Boolean> deleteByTagString(@PathVariable String tagString) {
        try {
            boolean deleted = surveyTagService.deleteByTagString(tagString);
            return ResponseEntity.ok(deleted);
        } catch (QuestionTagNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
