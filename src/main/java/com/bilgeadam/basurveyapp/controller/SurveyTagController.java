package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateTagDto;
import com.bilgeadam.basurveyapp.dto.response.TagResponseDto;
import com.bilgeadam.basurveyapp.entity.tags.SurveyTag;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTagNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.SurveyTagNotFoundException;
import com.bilgeadam.basurveyapp.services.SurveyTagService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    // kaldırılacak.
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    @Operation(
            summary = "Etiket Oluşturma",
            description = "Yeni bir etiket oluşturan metot. Yalnızca ADMIN veya MANAGER rolüne sahip kullanıcılar tarafından erişilebilir. ",
            hidden = true,
            tags = {"Tag Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Oluşturulacak etiketin bilgilerini içeren istek gövdesi. tagString-mainTagOid",
                    required = true
            )
    )
    @Hidden
    public ResponseEntity<SurveyTag> createTag(@RequestBody @Valid CreateTagDto dto ){
        SurveyTag createdTag = surveyTagService.createTag(dto);
        return ResponseEntity.ok(createdTag);
    }
    // kaldırılacak.
    @PutMapping("/update-by-tag-string/{tag-string}")
    @Operation(summary = "Belirtilen tag stringine sahip olan survey tag'in güncellenmesini sağlayan metot")
    @Hidden
    public ResponseEntity<SurveyTag> updateTagByTagString(
            @PathVariable("tag-string") String tagString,
            @PathVariable("new-tag-string") String newTagString
           ) {
        try {
            SurveyTag surveyTag = surveyTagService.updateTagByTagString(tagString,newTagString);
            return ResponseEntity.ok(surveyTag);
        } catch (SurveyTagNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // kaldırılacak.
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findall")
    @Hidden
    public ResponseEntity<List<TagResponseDto>> findAll() {
        List<TagResponseDto> responseDtoList = surveyTagService.findAll();
        return ResponseEntity.ok(responseDtoList);
    }
    // bu metotda main tag e taşınmalı
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete/{tagString}")
    @Operation(
            summary = "Question Tag'ini Sil",
            description = "Tag stringine göre bulunan question tag'in silinmesini sağlayan metot. #116",
            tags = {"Survey Tag Controller"},
            parameters = {
                    @Parameter(name = "tagString", description = "Silinecek question tag'in tag stringi.", required = true)
            }
    )
    public ResponseEntity<Boolean> deleteByTagString(@PathVariable String tagString) {
        try {
            boolean deleted = surveyTagService.deleteByTagString(tagString);
            return ResponseEntity.ok(deleted);
        } catch (QuestionTagNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
