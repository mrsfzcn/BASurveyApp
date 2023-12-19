package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateTagDto;
import com.bilgeadam.basurveyapp.dto.response.TagResponseDto;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTagNotFoundException;
import com.bilgeadam.basurveyapp.services.QuestionTagService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Operation(
            summary = "Yeni Soru Etiketi Oluştur",
            description = "String türünde bir etiket adı girilerek yeni bir soru etiketi oluşturulmasını sağlayan metot. #55",
            tags = {"Question Tag Controller"}
    )
    @Hidden
    public ResponseEntity<QuestionTag> createTag(@RequestBody @Valid CreateTagDto dto) {
        QuestionTag createdTag = questionTagService.createTag(dto);
        return ResponseEntity.ok(createdTag);
    }

    @PutMapping("/update-tag-by-tag-string/{tag-string}")
    @Operation(
            summary = "Soru Etiketini Güncelle",
            description = "Belirtilen etiket adına sahip olan soru etiketini güncellemeyi sağlayan metot. #56",
            tags = {"Question Tag Controller"}
    )
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
    @Operation(
            summary = "Tüm Soru Etiketlerini Görüntüle",
            description = "Sistemdeki tüm soru etiketlerini görüntülemeyi sağlayan metot. #57",
            tags = {"Question Tag Controller"}
    )
    public ResponseEntity<List<TagResponseDto>> findAll() {
        List<TagResponseDto> responseDtoList = questionTagService.findAll();
        return ResponseEntity.ok(responseDtoList);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete-by-tag-string")
    @Operation(
            summary = "Soru Etiketini Tag Stringine Göre Sil",
            description = "Belirtilen tag stringine sahip olan soru etiketini silen metot. #58",
            tags = {"Question Tag Controller"},
            parameters = {
                    @Parameter(
                            name = "tagString",
                            description = "Silinecek soru etiketinin tag stringi.",
                            required = true
                    )
            }
    )
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
