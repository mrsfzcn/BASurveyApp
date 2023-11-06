package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateBranchRequestDto;
import com.bilgeadam.basurveyapp.dto.response.BranchModelResponse;
import com.bilgeadam.basurveyapp.dto.response.MessageResponseDto;
import com.bilgeadam.basurveyapp.entity.Branch;
import com.bilgeadam.basurveyapp.repositories.IBranchRepository;
import com.bilgeadam.basurveyapp.services.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/branch")
public class BranchController {

    private final BranchService service;

    @PostMapping("/create")
    public ResponseEntity<MessageResponseDto> create(@RequestBody @Valid CreateBranchRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/get-data-from-base-api")
    public ResponseEntity<List<BranchModelResponse>> getDataFromBaseApi() {
        return ResponseEntity.ok(service.getAllDataFromBaseApi());
    }
}
