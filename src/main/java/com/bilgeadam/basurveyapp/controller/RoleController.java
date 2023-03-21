package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateRoleDto;
import com.bilgeadam.basurveyapp.dto.response.CreateRoleResponseDto;
import com.bilgeadam.basurveyapp.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Eralp Nitelik
 */
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/createrole")
    public ResponseEntity<CreateRoleResponseDto> createRole(CreateRoleDto dto) {
        return ResponseEntity.ok(roleService.createRole(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/findroles")
    public ResponseEntity<List<String>> findRoles() {
        return ResponseEntity.ok(roleService.findRoleStrings());
    }
}