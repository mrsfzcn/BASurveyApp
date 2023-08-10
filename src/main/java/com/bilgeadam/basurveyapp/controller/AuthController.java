package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.AuthenticationResponseDto;
import com.bilgeadam.basurveyapp.dto.response.RegenerateQrCodeResponse;
import com.bilgeadam.basurveyapp.dto.response.RegisterResponseDto;
import com.bilgeadam.basurveyapp.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update-login-credentials")
    @Operation(summary = "")
    public ResponseEntity<AuthenticationResponseDto> updateLoginCredentials(@RequestBody @Valid ChangeLoginRequestDto request) {
        return ResponseEntity.ok(authService.updateLoginCredentials(request));
    }

    @PostMapping("/switch-authorization-roles")
    @Operation(summary = "Bir role ait token üzerinden o rolün değiştirilmesini sağlayan metot.")
    public ResponseEntity<AuthenticationResponseDto> switchAuthorizationRoles(@RequestBody @Valid ChangeAuthorizedRequestDto request) {
        return ResponseEntity.ok(authService.switchAuthorizationRoles(request));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Boolean> verifyCode(@RequestBody VerifyCodeRequestDto verifyCodeRequestDto){
        return ResponseEntity.ok(authService.verifyCode(verifyCodeRequestDto));
    }

    @PutMapping("/regenerate-qr-code")
    public ResponseEntity<RegenerateQrCodeResponse> regenerateQrCode(@RequestBody RegenerateQrCodeRequest regenerateQrCodeRequest){
        return ResponseEntity.ok(authService.regenerateQrCode(regenerateQrCodeRequest));
    }
}
