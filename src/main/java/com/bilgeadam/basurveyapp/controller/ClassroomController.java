package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.services.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/classroom")
@RequiredArgsConstructor
public class ClassroomController {
    private final ClassroomService classroomService;
}


