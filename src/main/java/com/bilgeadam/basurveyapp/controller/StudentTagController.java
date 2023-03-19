package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateStudentTagRequestDto;
import com.bilgeadam.basurveyapp.services.StudentTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/studenttag")
@RequiredArgsConstructor
public class StudentTagController {

    private final StudentTagService studentTagService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<Boolean> createStudentTag(@RequestBody CreateStudentTagRequestDto dto){

        return ResponseEntity.ok(studentTagService.createStudentTag(dto));

    }


}
