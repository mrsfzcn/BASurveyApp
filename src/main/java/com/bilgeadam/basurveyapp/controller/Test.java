package com.bilgeadam.basurveyapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Test {
    @GetMapping()
    @Operation(
            summary = "Test Endpoint",
            description = "Uygulamanın çalışıp çalışmadığını test etmek için kullanılan endpoint.",
            tags = {"Test Controller"}
    )
    public String test() {
        return "BASurveyApp";
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/student")
    @Operation(
            summary = "Student Test Endpoint",
            description = "Öğrenci yetkisini test etmek için kullanılan endpoint.",
            tags = {"Test Controller"}
    )
    public ResponseEntity<String> testUser() {
        return ResponseEntity.ok("Student test successful!");
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/manager")
    @Operation(
            summary = "Manager Test Endpoint",
            description = "Manager yetkisini test etmek için kullanılan endpoint.",
            tags = {"Test Controller"}
    )
    public ResponseEntity<String> testManager() {
        return ResponseEntity.ok("Manager test successful!");
    }

    @PreAuthorize("hasRole('MASTER_TRAINER')")
    @GetMapping("/m-trainer")
    @Operation(
            summary = "Master Trainer Test Endpoint",
            description = "Master Trainer yetkisini test etmek için kullanılan endpoint.",
            tags = {"Test Controller"}
    )
    public ResponseEntity<String> testMT() {
        return ResponseEntity.ok("MT test successful!");
    }

    @PreAuthorize("hasRole('ASSISTANT_TRAINER')")
    @GetMapping("/a-trainer")
    @Operation(
            summary = "Assistant Trainer Test Endpoint",
            description = "Asistan eğitmen yetkisini test etmek için kullanılan endpoint.",
            tags = {"Test Controller"}
    )
    public ResponseEntity<String> testAT() {
        return ResponseEntity.ok("AT test successful!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    @Operation(
            summary = "Admin Test Endpoint",
            description = "Admin yetkisini test etmek için kullanılan endpoint.",
            tags = {"Test Controller"}
    )
    public ResponseEntity<String> testAdmin() {
        return ResponseEntity.ok("Admin test successful!");
    }


}
