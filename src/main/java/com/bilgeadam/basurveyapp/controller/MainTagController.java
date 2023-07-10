package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateMainTagRequestDto;
import com.bilgeadam.basurveyapp.services.MainTagService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Muhammed Furkan Türkmen
 */
@RestController
@RequestMapping("/maintag")
@RequiredArgsConstructor
public class MainTagController {
    private final MainTagService mainTagService;
    @PostMapping("/createmaintag")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "tag olusturma islemi. tag basliklarinda belirtilen isimde tag olusturur. (QUESTION,STUDENT,SURVEY,TRAINER)")
    public ResponseEntity<Boolean> createMainTag(@RequestBody CreateMainTagRequestDto dto){
        mainTagService.createTag(dto);
        return ResponseEntity.ok(true);
    }
}
