package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.AuthenticationResponseDto;
import com.bilgeadam.basurveyapp.dto.response.RegisterResponseDto;
import com.bilgeadam.basurveyapp.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Eralp Nitelik
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Mail, şifre, isim, soyisim ve rol girilerek yeni kullanıcı oluşturan metot. #5")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Mail ve şifre ile giriş yapılmasını sağlayan metot. #0")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody @Valid LoginRequestDto request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    /**
     * @param request
     * authorizedToken must kept in front-end for change between accounts
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update-login-credentials")
    //TODO "changelogin" ismi değişecek --> (bug olarak board' a eklendi)
    @Operation(summary = "")
    public ResponseEntity<AuthenticationResponseDto> changeLogin(@RequestBody @Valid ChangeLoginRequestDto request) {
        return ResponseEntity.ok(authService.changeLogin(request));
    }

    @PostMapping("/switch-authorization-roles")
    @Operation(summary = "Bir role ait token üzerinden o rolün değiştirilmesini sağlayan metot.")
    public ResponseEntity<AuthenticationResponseDto> switchAuthorizationRoles(@RequestBody @Valid ChangeAuthorizedRequestDto request) {
        return ResponseEntity.ok(authService.switchAuthorizationRoles(request));
    }

    @PostMapping("/verifycode")
    public ResponseEntity<Boolean> verifyCode(@RequestBody VerifyCodeRequestDto verifyCodeRequestDto){
        return ResponseEntity.ok(authService.verifyCode(verifyCodeRequestDto));
    }

}
