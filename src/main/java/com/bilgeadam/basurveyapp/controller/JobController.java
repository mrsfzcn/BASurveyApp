package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.services.jobservices.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(
            summary = "Api'den Veri Alma Görevini Yeniden Zamanlama",
            description = "Api'den veri alma görevini belirtilen saat ve dakikaya göre yeniden zamanlamak için kullanılan endpoint.",
            tags = {"Job Controller"},
            parameters = {
                    @Parameter(
                            name = "expression",
                            description = "String girilecek saat bilgisi",
                            example = "13:35"
                    )
            }
    )
    public ResponseEntity<Boolean> rescheduleGetDatasFromApi(String expression) {
        jobService.rescheduleGetDatasFromApi(expression);
        return ResponseEntity.ok(true);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/get-current-update-time")
    @Operation(
            summary = "Güncel Zamanlama Saatini Alma",
            description = "Api'den veri alma görevinin güncel zamanlama saatini almak için kullanılan endpoint.",
            tags = {"Job Controller"}
    )
    public ResponseEntity<String> getCurrentUpdateTime() {
        return ResponseEntity.ok(jobService.getCurrentUpdateTime());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/update-survey-app-data")
    @Operation(
            summary = "Anket Uygulama Verilerini Anında Güncelle",
            description = "Anket uygulama verilerini API'den verileri çekerek anında güncellemek için kullanılan endpoint.",
            tags = {"Job Controller"}
    )
    public ResponseEntity<Boolean> instantUpdate() {
        jobService.getDatasFromApi();
        return ResponseEntity.ok(true);
    }




}
