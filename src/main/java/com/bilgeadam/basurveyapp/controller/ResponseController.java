package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.services.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/response")
@RequiredArgsConstructor
public class ResponseController {
    private final ResponseService responseService;

    @GetMapping("/test")
    public String test() {
        return "response";
    }
}
