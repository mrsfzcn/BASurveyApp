package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateBranchRequestDto;
import com.bilgeadam.basurveyapp.dto.request.FindByNameAndCityRequestDto;
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

    @DeleteMapping("/soft-delete-by/{oid}")
    public ResponseEntity<Boolean> deleteBranchByOid(@PathVariable Long oid) {
        return ResponseEntity.ok(service.deleteBranchByOid(oid));
    }

    @GetMapping("/find-by-name/{name}")
    public ResponseEntity<List<Branch>> findBranchesByName(@PathVariable String name) {
        return ResponseEntity.ok(service.findBranchesByName(name));
    }

    @GetMapping("/find-by-name-and-city")
    public ResponseEntity<Branch> findByNameAndCity(FindByNameAndCityRequestDto dto) {
        return ResponseEntity.ok(service.findByNameAndCity(dto));
    }

    @GetMapping("/find-by-city/{city}")
    public ResponseEntity<List<Branch>> findByCity(@PathVariable String city) {
        return ResponseEntity.ok(service.findByCity(city));
    }

    @GetMapping("/find-by-apiId/{apiId}")
    public ResponseEntity<Branch> findByApiId(@PathVariable String apiId) {
        return ResponseEntity.ok(service.findByApiId(apiId));
    }

}
