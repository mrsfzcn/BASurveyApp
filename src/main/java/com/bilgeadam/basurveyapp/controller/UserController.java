package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.AssignRoleToUserRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UserUpdateRequestDto;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/managers")
    ResponseEntity<List<ManagerResponseDto>> getManagerList() {
        return ResponseEntity.ok(userService.getManagerList());
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/admins")
    ResponseEntity<List<AdminResponseDto>> getAdminList() {

        return ResponseEntity.ok(userService.getAdminList());
    }

    //TODO front-end de ilerleyen aşamada test edilecek. 12.04.2023
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/page")
    ResponseEntity<Page<User>> getUserPage(Pageable pageable) {
        return ResponseEntity.ok(userService.getUserPage(pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{userId}")
    ResponseEntity<UserSimpleResponseDto> findByOid(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.findByOid(userId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update/{userEmail}")
    ResponseEntity<Void> updateUser(@PathVariable("userEmail") String userEmail, @RequestBody UserUpdateRequestDto dto) {
        userService.updateUser(userEmail, dto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete/{userId}")
    ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/trainersandstudents")
    ResponseEntity<List<UserTrainersAndStudentsResponseDto>> getTrainersAndStudentsList(String jwtToken) {
        return ResponseEntity.ok(userService.getTrainersAndStudentsList(jwtToken));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/assingroletouser")
    ResponseEntity<Boolean> assignRoleToUser(@RequestBody @Valid AssignRoleToUserRequestDto request) {

        return ResponseEntity.ok(userService.assignRoleToUser(request));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @GetMapping("/find-all-user-details")
    ResponseEntity<List<FindAllUserDetailsResponseDto>> findAllUserDetails() {
        return ResponseEntity.ok(userService.findAllUserDetails());
    }



}
