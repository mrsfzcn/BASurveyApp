package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateRoleDto;
import com.bilgeadam.basurveyapp.dto.response.CreateRoleResponseDto;
import com.bilgeadam.basurveyapp.entity.Role;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Eralp Nitelik
 */
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public boolean userHasRole(User user, String role) {
        return user.getRoles().stream().map(Role::getRole).anyMatch(roleString -> roleString.equals(role));
    }

    public List<String> findRoleStrings() {
        return roleRepository.findAllActive().stream().map(Role::getRole).collect(Collectors.toList());
    }

    public List<Role> findRoles() {
        return roleRepository.findAllActive();
    }

    public CreateRoleResponseDto createRole(CreateRoleDto dto) {
        Role role = roleRepository.save(Role.builder()
                .role(dto.getRole().toUpperCase())
                .build());
        return CreateRoleResponseDto.builder()
                .roleOid(role.getOid())
                .role(role.getRole())
                .build();
    }
}
