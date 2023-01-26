package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.LoginRequestDto;
import com.bilgeadam.basurveyapp.dto.request.RegisterRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AuthenticationResponseDto;
import com.bilgeadam.basurveyapp.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Eralp Nitelik
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody @Valid LoginRequestDto request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
