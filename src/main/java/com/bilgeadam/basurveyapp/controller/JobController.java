package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.services.jobservices.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/reschedule")
    public ResponseEntity<Boolean> rescheduleGetDatasFromApi(String expression) {
        jobService.rescheduleGetDatasFromApi(expression);
        return ResponseEntity.ok(true);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/get-current-update-time")
    public ResponseEntity<String> getCurrentUpdateTime() {
        return ResponseEntity.ok(jobService.getCurrentUpdateTime());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/update-survey-app-data")
    public ResponseEntity<Boolean> instantUpdate() {
        jobService.getDatasFromApi();
        return ResponseEntity.ok(true);
    }




}
