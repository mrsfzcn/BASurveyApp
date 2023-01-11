package com.bilgeadam.basurveyapp.controller;


import com.bilgeadam.basurveyapp.services.QuestionNumericService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/questionnumeric")
public class QuestionNumericController {

    private final QuestionNumericService questionNumericService;
}
