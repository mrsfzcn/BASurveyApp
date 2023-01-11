package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.services.ResponseNumericService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/responseNumeric")
@RequiredArgsConstructor
public class ResponseNumericController {
    private final ResponseNumericService responseNumericService;
}
