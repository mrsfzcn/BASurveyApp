package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateRoleDto;
import com.bilgeadam.basurveyapp.dto.response.CreateRoleResponseDto;
import com.bilgeadam.basurveyapp.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Eralp Nitelik
 */
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

//    @PreAuthorize("hasAnyRole('ADMIN')")
@PostMapping("/createrole")
@Operation(summary = "String girdisi ile yeni bir rol oluşturulmasını sağlayan metot. #01")
    public ResponseEntity<CreateRoleResponseDto> createRole(@RequestBody CreateRoleDto dto) {
        return ResponseEntity.ok(roleService.createRole(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/findroles")
    @Operation(summary = "Tüm rollerin görüntülenmesini sağlayan metot.")
    public ResponseEntity<List<String>> findRoleStrings() {
        return ResponseEntity.ok(roleService.findRoleStrings());
    }
}