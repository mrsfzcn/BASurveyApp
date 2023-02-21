package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.entity.SubTag;
import com.bilgeadam.basurveyapp.entity.Tag;
import com.bilgeadam.basurveyapp.services.SubTagService;
import com.bilgeadam.basurveyapp.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subtag")
@RequiredArgsConstructor
public class SubTagController {
    private SubTagService subtagService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<Boolean> createsubTag(@RequestBody @Valid SubTag subTag) {
        return ResponseEntity.ok(subtagService.createTag(subTag));
    }
}
