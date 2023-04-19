package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.ChangeAuthorizedRequestDto;
import com.bilgeadam.basurveyapp.dto.request.ChangeLoginRequestDto;
import com.bilgeadam.basurveyapp.dto.request.LoginRequestDto;
import com.bilgeadam.basurveyapp.dto.request.RegisterRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AuthenticationResponseDto;
import com.bilgeadam.basurveyapp.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    /**
     * @param request
     * authorizedToken must kept in front-end for change between accounts
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/changelogin")
    //TODO "changelogin" ismi değişecek
    public ResponseEntity<AuthenticationResponseDto> changeLogin(@RequestBody @Valid ChangeLoginRequestDto request) {
        return ResponseEntity.ok(authService.changeLogin(request));
    }

    @PostMapping("/changeauthorized")
    //TODO "changeauthorized" ismi "switchauthorizationroles" olarak değişecek
    public ResponseEntity<AuthenticationResponseDto> changeAuthorized(@RequestBody @Valid ChangeAuthorizedRequestDto request) {
        return ResponseEntity.ok(authService.changeAuthorized(request));
    }
}
