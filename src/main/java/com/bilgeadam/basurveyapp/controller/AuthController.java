package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.AuthenticationResponseDto;
import com.bilgeadam.basurveyapp.dto.response.RegenerateQrCodeResponse;
import com.bilgeadam.basurveyapp.dto.response.RegisterResponseDto;
import com.bilgeadam.basurveyapp.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Kullanıcı Kaydı",
            description = "Mail, şifre, isim, soyisim ve rol girilerek yeni kullanıcı oluşturan metot. #5\n\n " +
                    "1. Eğer kullanıcı admin veya manager rolüne sahipse, ilgili rolle ilişkilendirilen bir yönetici (Manager) oluşturulur.\n " +
                    "2. Eğer kullanıcı student rolüne sahipse, bir öğrenci (Student) oluşturulur.\n " +
                    "3. Eğer kullanıcı master trainer veya assistant trainer rolüne sahipse, ilgili rolle ilişkilendirilen bir eğitmen (Trainer) oluşturulur. #0",
            tags = {"Auth Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Yeni kullanıcının bilgilerini içeren istek gövdesi. password-firstName-lastName-roles",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Giriş bilgileri başarıyla güncellendi. Yeni kimlik doğrulama token'ı ile birlikte."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen kullanıcı veya yetkilendirilmiş kullanıcı bulunamadı."
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Giriş bilgileri başka bir admin veya yönetici hesabına değiştirilemez."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Bilinmeyen bir hata oluştu."
                    )
            }
    )
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    @Operation(
            summary = "Yetkilendirme",
            description = "Mail ve şifre ile giriş yapılmasını sağlayan metot. Eğer kullanıcı iki faktörlü kimlik doğrulamayı etkinleştirmemişse, yeni bir QR kodu ile birlikte kimlik doğrulama token'ı oluşturulur. #1",
            tags = {"Auth Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Kullanıcının kimlik doğrulama bilgilerini içeren istek gövdesi.",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Yetkilendirmeler başarıyla değiştirildi. Yeni kimlik doğrulama token'ı ile birlikte."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen kullanıcı bulunamadı."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Belirtilen yetkilendirme rolü bulunamadı veya mevcut rol ile uyumsuz."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Bilinmeyen bir hata oluştu."
                    )
            }
    )
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody @Valid LoginRequestDto request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update-login-credentials")
    @Operation(
            summary = "Giriş Bilgilerini Güncelleme",
            description = "Belirtilen kullanıcının giriş bilgilerini güncelleyerek yeni bir kimlik doğrulama token'ı oluşturur. Yalnızca ADMIN veya MANAGER rolüne sahip kullanıcılar tarafından erişilebilir. userEmail-authorizedToken\n\n" +
                    "İşlem adımları:\n" +
                    "1. Yetkilendirilmiş kullanıcı belirlenir ve eğer bulunamazsa 'User is not found' hatası fırlatılır.\n" +
                    "2. Güncellenecek kullanıcı belirlenir ve eğer bulunamazsa 'Username does not exist.' hatası fırlatılır.\n" +
                    "3. Eğer kullanıcı ADMIN veya MANAGER rolüne sahipse, 'You can't change login to other admin or manager account' hatası fırlatılır.\n" +
                    "4. Kullanıcının rolüne göre yetkilendirme rolü atanır.\n" +
                    "5. Kullanıcı bilgileri güncellenir ve yeni bir kimlik doğrulama token'ı oluşturularak döndürülür. #2",
            tags = {"Auth Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Güncellenecek kullanıcının yeni giriş bilgilerini içeren istek gövdesi.",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Giriş bilgileri başarıyla güncellendi. Yeni kimlik doğrulama token'ı ile birlikte."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen kullanıcı veya yetkilendirilmiş kullanıcı bulunamadı."
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Giriş bilgileri başka bir admin veya yönetici hesabına değiştirilemez."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Bilinmeyen bir hata oluştu."
                    )
            }
    )
    public ResponseEntity<AuthenticationResponseDto> updateLoginCredentials(@RequestBody @Valid ChangeLoginRequestDto request) {
        return ResponseEntity.ok(authService.updateLoginCredentials(request));
    }

    @PostMapping("/switch-authorization-roles")
    @Operation(
            summary = "Kullanıcı Yetkilendirmelerini Değiştirme",
            description = "Belirtilen kullanıcının yetkilendirme rollerini değiştirerek yeni bir kimlik doğrulama token'ı oluşturur. Kullanıcının mevcut rolü, istenen yetkilendirme rolü ile uyumlu olmalıdır. authorizedRole-authorizedToken #3",
            tags = {"Auth Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Yetkilendirme rollerini değiştirecek kullanıcının istenen rollerini ve tokenlerini içeren istek gövdesi.",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Yetkilendirmeler başarıyla değiştirildi. Yeni kimlik doğrulama token'ı ile birlikte."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen kullanıcı bulunamadı."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Belirtilen yetkilendirme rolü bulunamadı veya mevcut rol ile uyumsuz."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Bilinmeyen bir hata oluştu."
                    )
            }
    )
    public ResponseEntity<AuthenticationResponseDto> switchAuthorizationRoles(@RequestBody @Valid ChangeAuthorizedRequestDto request) {
        return ResponseEntity.ok(authService.switchAuthorizationRoles(request));
    }

    @PostMapping("/verify-code")
    @Operation(
            summary = "İki Faktörlü Kimlik Doğrulama",
            description = "Belirtilen kullanıcının iki faktörlü kimlik doğrulama kodunu doğrular. İki faktörlü kimlik doğrulama için kullanılan kod, kullanıcının belirtilen e-posta adresine atanmış bir JWT içerisinde bulunmalıdır. twoFactoryKey-token #4",
            tags = {"Auth Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Doğrulanacak iki faktörlü kimlik doğrulama kodunu ve token'ını içeren istek gövdesi.",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Kod başarıyla doğrulandı."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen kullanıcı bulunamadı."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Belirtilen iki faktörlü kimlik doğrulama kodu geçerli değil veya kullanıcının iki faktörlü kimlik doğrulama özelliği etkin değil."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Bilinmeyen bir hata oluştu."
                    )
            }
    )
    public ResponseEntity<Boolean> verifyCode(@RequestBody VerifyCodeRequestDto verifyCodeRequestDto){
        return ResponseEntity.ok(authService.verifyCode(verifyCodeRequestDto));
    }

    @PutMapping("/regenerate-qr-code")
    @Operation(
            summary = "QR Kodunu Yeniden Oluşturma",
            description = "Belirtilen kullanıcının iki faktörlü kimlik doğrulama için QR kodunu yeniden oluşturur ve günceller. Yeni QR kodu, kullanıcının e-posta adresine atanmış bir JWT içerisinde bulunmalıdır. token #5",
            tags = {"Auth Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Yeniden oluşturulacak QR kodu ve bu işlem için gerekli kimlik doğrulama token'ını içeren istek gövdesi.",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "QR kodu başarıyla yeniden oluşturuldu ve güncellendi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen kullanıcı bulunamadı."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Bilinmeyen bir hata oluştu."
                    )
            }
    )
    public ResponseEntity<RegenerateQrCodeResponse> regenerateQrCode(@RequestBody RegenerateQrCodeRequest regenerateQrCodeRequest){
        return ResponseEntity.ok(authService.regenerateQrCode(regenerateQrCodeRequest));
    }
}
