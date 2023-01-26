package com.bilgeadam.basurveyapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Test {
    @GetMapping()
    public String test() {
        return "BASurveyApp";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'MASTER_TRAINER', 'ASISTANT_TRAINER', 'STUDENT')")
    @GetMapping("/student")
    public ResponseEntity<String> testUser() {
        return ResponseEntity.ok("Student test successful!");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/manager")
    public ResponseEntity<String> testManager() {
        return ResponseEntity.ok("Manager test successful!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<String> testAdmin() {
        return ResponseEntity.ok("Admin test successful!");
    }


}
