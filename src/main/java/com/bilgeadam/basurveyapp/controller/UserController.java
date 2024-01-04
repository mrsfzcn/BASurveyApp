package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.AssignRoleToUserRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UserUpdateRequestDto;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "Manager Listesini Getirme",
            description = "Manager kullanıcılarının listesini getirir. #134",
            tags = {"User Controller"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Manager listesi başarıyla alındı."
                    )
            }
    )
    ResponseEntity<List<ManagerResponseDto>> getManagerList() {
        return ResponseEntity.ok(userService.getManagerList());
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/admins")
    @Operation(
            summary = "Admin Listesini Getirme",
            description = "Admin kullanıcılarının listesini getirir. #135",
            tags = {"User Controller"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Admin listesi başarıyla alındı."
                    )
            }
    )
    ResponseEntity<List<AdminResponseDto>> getAdminList() {
        return ResponseEntity.ok(userService.getAdminList());
    }

    //TODO front-end de ilerleyen aşamada test edilecek. 12.04.2023
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/page")
    @Operation(
            summary = "Kullanıcı Sayfasını Getirme",
            description = "Sayfalı kullanıcı listesini getirir. #136",
            tags = {"User Controller"},
            parameters = {
                    @Parameter(name = "pageable", description = "Sayfalama ve sıralama parametrelerini içeren istek gövdesi.", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Kullanıcı listesi başarıyla sayfalandı."
                    )
            }
    )
    ResponseEntity<Page<User>> getUserPage(Pageable pageable) {
        return ResponseEntity.ok(userService.getUserPage(pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{user-id}")
    @Operation(
            summary = "Kullanıcı Bulma",
            description = "Kullanıcıyı ID'ye göre bulma. #137",
            tags = {"User Controller"},
            parameters = {
                    @Parameter(
                            name = "user-id",
                            description = "User ID",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Manager listesi başarıyla alındı."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Kullanıcı bulunamadı hatası."
                    )
            }
    )
    ResponseEntity<UserSimpleResponseDto> findByOid(@PathVariable("user-id") Long userId) {
        return ResponseEntity.ok(userService.findByOid(userId));
    }


    @GetMapping("/find-user-by-email-token/{token}")
    @Operation(
            summary = "Email Token ile Kullanıcı Bulma",
            description = "Email Token kullanarak kullanıcıyı bulma. #138",
            tags = {"User Controller"},
            parameters = {
                    @Parameter(
                            name = "token",
                            description = "Email token",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Kullanıcı başarıyla alındı."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Token hatası."
                    )
            }
    )
    ResponseEntity<UserSimpleResponseDto> findByEmailToken(@PathVariable String token) {
        return ResponseEntity.ok(userService.findByEmailToken(token));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update/{user-email}")
    @Operation(
            summary = "Kullanıcı Güncelleme",
            description = "Belirtilen e-posta adresine sahip kullanıcıyı güncelleme. #139",
            tags = {"User Controller"},
            parameters = {
                    @Parameter(
                            name = "user-email",
                            description = "User Email",
                            required = true
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Güncellenecek kullanıcı bilgilerini içeren istek gövdesi. firstName-lastName-email-authorizedRole",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Kullanıcı başarıyla güncellendi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Kullanıcı bulunamadı hatası."
                    )
            }
    )
    ResponseEntity<User> updateUser(@PathVariable("user-email") String userEmail, @RequestBody UserUpdateRequestDto dto) {
        userService.updateUser(userEmail, dto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete/{user-id}")
    @Operation(
            summary = "Kullanıcı Silme",
            description = "Belirtilen kullanıcıyı silme. #140",
            tags = {"User Controller"},
            parameters = {
                    @Parameter(
                            name = "user-id",
                            description = "User ID",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Kullanıcı başarıyla silindi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Kullanıcı bulunamadı hatası."
                    )
            }
    )
    ResponseEntity<Void> deleteUser(@PathVariable("user-id") Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/trainers-and-students")
    @Operation(
            summary = "Eğitmenler ve Öğrenciler Listesi",
            description = "Eğitmenler ve onlara kayıtlı öğrencilerin listesini getirme. #141",
            tags = {"User Controller"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Eğitmenler ve öğrenciler başarıyla listelendi."
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Access denied hatası."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Kullanıcı bulunamadı hatası."
                    )
            }
    )
    ResponseEntity<List<UserTrainersAndStudentsResponseDto>> getTrainersAndStudentsList(String jwtToken) {
        return ResponseEntity.ok(userService.getTrainersAndStudentsList(jwtToken));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/assing-role-to-user")
    @Operation(
            summary = "Kullanıcıya Rol Atama",
            description = "Belirtilen kullanıcıya belirtilen rolü atama. #142",
            tags = {"User Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Kullanıcıya atanacak rolü ve kullanıcı kimliğini içeren istek gövdesi. role-userEmail",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Kullanıcıya başarıyla rol atandı."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Kullanıcı veya rol bulunamadı hatası."
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Rol zaten atanmış hatası."
                    )
            }
    )
    ResponseEntity<Boolean> assignRoleToUser(@RequestBody @Valid AssignRoleToUserRequestDto request) {
        return ResponseEntity.ok(userService.assignRoleToUser(request));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @GetMapping("/find-all-user-details")
    @Operation(
            summary = "Tüm Kullanıcı Detaylarını Getirme",
            description = "Sistemde kayıtlı olan tüm kullanıcıların detaylarını getirir. #143",
            tags = {"User Controller"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tüm kullanıcılar başarıyla getirildi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Kullanıcı bulunamadı hatası."
                    )
            }
    )
    ResponseEntity<List<FindAllUserDetailsResponseDto>> findAllUserDetails() {
        return ResponseEntity.ok(userService.findAllUserDetails());
    }
}
