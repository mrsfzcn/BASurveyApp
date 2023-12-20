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

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/")
    @Operation(
            summary = "Yeni Rol Oluştur",
            description = "String girdisi ile yeni bir rol oluşturur. #79",
            tags = {"Role Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Yeni rolün adını içeren istek gövdesi. role",
                    required = true
            )
    )
    public ResponseEntity<CreateRoleResponseDto> createRole(@RequestBody CreateRoleDto dto) {
        return ResponseEntity.ok(roleService.createRole(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/roles")
    @Operation(
            summary = "Tüm Rollerin Görüntülenmesi",
            description = "Tüm rollerin görüntülenmesini sağlar. #80",
            tags = {"Role Controller"}
    )
    public ResponseEntity<List<String>> findRoleStrings() {
        return ResponseEntity.ok(roleService.findRoleStrings());
    }
}