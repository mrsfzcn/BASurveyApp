package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateBranchRequestDto;
import com.bilgeadam.basurveyapp.dto.request.FindByNameAndCityRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateBranchRequestDto;
import com.bilgeadam.basurveyapp.dto.response.BranchModelResponse;
import com.bilgeadam.basurveyapp.dto.response.MessageResponseDto;
import com.bilgeadam.basurveyapp.entity.Branch;
import com.bilgeadam.basurveyapp.repositories.IBranchRepository;
import com.bilgeadam.basurveyapp.services.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/branch")
public class BranchController {

    private final BranchService service;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<MessageResponseDto> create(@RequestBody @Valid CreateBranchRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/get-data-from-base-api")
    public ResponseEntity<List<BranchModelResponse>> getDataFromBaseApi() {
        return ResponseEntity.ok(service.getAllDataFromBaseApi());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/soft-delete-by/{oid}")
    public ResponseEntity<Boolean> deleteBranchByOid(@PathVariable Long oid) {
        return ResponseEntity.ok(service.deleteBranchByOid(oid));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-by-name/{name}")
    public ResponseEntity<List<Branch>> findBranchesByName(@PathVariable String name) {
        return ResponseEntity.ok(service.findBranchesByName(name));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-by-name-and-city")
    public ResponseEntity<Branch> findByNameAndCity(FindByNameAndCityRequestDto dto) {
        return ResponseEntity.ok(service.findByNameAndCity(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-by-city/{city}")
    public ResponseEntity<List<Branch>> findByCity(@PathVariable String city) {
        return ResponseEntity.ok(service.findByCity(city));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-by-apiId/{apiId}")
    public ResponseEntity<Branch> findByApiId(@PathVariable String apiId) {
        return ResponseEntity.ok(service.findByApiId(apiId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update")
    public ResponseEntity<MessageResponseDto> updateBranchByApiId(@RequestBody UpdateBranchRequestDto dto) {
        return ResponseEntity.ok(service.updateBranchByApiId(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/active-branches")
    public ResponseEntity<List<Branch>> activeBranches() {
        return ResponseEntity.ok(service.findAllActiveBranches());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/deleted-branches")
    public ResponseEntity<List<Branch>> deletedBranches() {
        return ResponseEntity.ok(service.findAllDeletedBranches());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/activate-branch/{oid}")
    public ResponseEntity<MessageResponseDto> activateBranch(@PathVariable Long oid) {
        return ResponseEntity.ok(service.activateBranch(oid));
    }
}
