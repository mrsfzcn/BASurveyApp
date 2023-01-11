package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.service.QuestionTextService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questionText")
@RequiredArgsConstructor
public class QuestionTextController {
    private final QuestionTextService questionTextService;
}
