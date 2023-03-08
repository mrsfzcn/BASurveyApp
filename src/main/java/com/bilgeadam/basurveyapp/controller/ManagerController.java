package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.entity.Manager;
import com.bilgeadam.basurveyapp.services.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/manager")
public class ManagerController {
    private final ManagerService managerService;
    @PostMapping("/create")
    public ResponseEntity<Boolean> createManager(@RequestBody Manager manager){
        return ResponseEntity.ok(managerService.createManager(manager));
    }
}
