package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.UserCreateRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UserUpdateRequestDto;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/list")
    ResponseEntity<List<User>> getUserList() {
        return ResponseEntity.ok(userService.getUserList());
    }

    @GetMapping("/page")
    ResponseEntity<Page<User>> getUserPage(Pageable pageable) {
        return ResponseEntity.ok(userService.getUserPage(pageable));
    }

    @GetMapping("/{userId}")
    ResponseEntity<User> findById(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.findByOid(userId));
    }

    @PostMapping("/create")
    ResponseEntity<User> createUser(@RequestBody UserCreateRequestDto dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @PostMapping("/update/{userId}")
    ResponseEntity<User> updateUser(@PathVariable("userId") Long userId, @RequestBody UserUpdateRequestDto dto) {
        return ResponseEntity.ok(userService.updateUser(userId, dto));
    }

    @PutMapping("/delete/{userId}")
    ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
