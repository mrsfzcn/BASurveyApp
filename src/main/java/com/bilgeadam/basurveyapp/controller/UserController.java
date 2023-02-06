package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.UserUpdateRequestDto;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.services.UserService;
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

    @GetMapping("/test")
    public String test() {
        return "hello";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'MASTER_TRAINER', 'ASISTANT_TRAINER', 'STUDENT')")
    @GetMapping("/list")
    ResponseEntity<List<User>> getUserList() {
        return ResponseEntity.ok(userService.getUserList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'MASTER_TRAINER', 'ASISTANT_TRAINER', 'STUDENT')")
    @GetMapping("/page")
    ResponseEntity<Page<User>> getUserPage(Pageable pageable) {
        return ResponseEntity.ok(userService.getUserPage(pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'MASTER_TRAINER', 'ASISTANT_TRAINER', 'STUDENT')")
    @GetMapping("/{userId}")
    ResponseEntity<User> findById(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.findByOid(userId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'MASTER_TRAINER', 'ASISTANT_TRAINER', 'STUDENT')")
    @PutMapping("/update/{userId}")
    ResponseEntity<User> updateUser(@PathVariable("userId") Long userId, @RequestBody UserUpdateRequestDto dto) {
        return ResponseEntity.ok(userService.updateUser(userId, dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'MASTER_TRAINER', 'ASISTANT_TRAINER', 'STUDENT')")
    @DeleteMapping("/delete/{userId}")
    ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
