package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.services.ResponseTextService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/responseText")
@RequiredArgsConstructor
public class ResponseTextController {
    private final ResponseTextService responseTextService;
}
