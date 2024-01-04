package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.request.FindTrainersByTagStringRequestDto;
import com.bilgeadam.basurveyapp.dto.request.FindUserByEmailRequestDto;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.services.TrainerTagService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainer-tag")
@RequiredArgsConstructor
public class TrainerTagController {
    private final TrainerTagService trainerTagService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    @Operation(summary = "java2, .net vb. gibi trainer tag oluşturmamızı sağlayan metot. #123")
    @Hidden
    public ResponseEntity<String> createTag(@RequestBody @Valid CreateTagDto dto ){
        trainerTagService.createTag(dto);
        return ResponseEntity.ok(dto.getTagString());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/find-active-by-id")
    @Operation(
            summary = "Aktif Trainer Tag Bulma",
            description = "Girilen oid'nin statusu aktifse, bu trainer tag'ının tag string'ini dönen metot. #124",
            tags = {"Trainer Tag Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Bulunacak aktif trainer tag'ının OID'sini içeren istek.",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Trainer tag başarıyla bulundu."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Trainer tag bulunamadı."
                    )
            }
    )
    public ResponseEntity<FindActiveTrainerTagByIdResponseDto> findActiveById(@RequestBody @Valid Long trainerTagOid ){
        return ResponseEntity.ok(trainerTagService.findActiveById(trainerTagOid));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping ("/delete")
    @Operation(
            summary = "Trainer Tag Silme",
            description = "Verilen trainer tag OID mevcutsa ve statusu aktifse, statusunu 'deleted' olarak güncelleyen metot. #125",
            tags = {"Trainer Tag Controller"},
            parameters = {
                    @Parameter(name = "trainerTagOid", description = "Silinecek trainer tag OID'sini içeren istek gövdesi.", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Trainer tag başarıyla silindi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Trainer tag bulunamadı."
                    )
            }
    )
    public ResponseEntity<Boolean> delete(@RequestBody @Valid Long trainerTagOid ){
        return ResponseEntity.ok(trainerTagService.delete(trainerTagOid));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/trainer-tags")
    @Operation(
            summary = "Trainer Tag Listeleme",
            description = "Trainer tag stringlerini (.net2, java1, java3 gibi) ve trainer tag OID'lerini dönen metot. #126",
            tags = {"Trainer Tag Controller"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Trainer tag listesi başarıyla alındı."
                    )
            }
    )
    public ResponseEntity<List<TrainerTagDetailResponseDto>> getTrainerTagList(){
        return ResponseEntity.ok(trainerTagService.getTrainerTagList());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/find-trainers-tags-by-email")
    @Operation(
            summary = "Trainer Taglarına Göre Listeleme",
            description = "Trainer email'i girildiğinde ilgili trainera ait tüm tag stringlerini (java1, java2 gibi) dönen metot. #127",
            tags = {"Trainer Tag Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Trainer email'ini içeren istek gövdesi.",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Trainer tagları başarıyla alındı."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Trainer veya tag bulunamadı."
                    )
            }
    )
    public ResponseEntity<GetTrainerTagsByEmailResponse> getTrainerTagsByEmail(@RequestBody @Valid FindUserByEmailRequestDto dto){
        return ResponseEntity.ok(trainerTagService.getTrainerTagsByEmail(dto.getEmail()));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','STUDENT')")
    @GetMapping("/find-trainers-by-trainer-tag/{token}")
    @Operation(
            summary = "Trainerları Tag'a Göre Listeleme",
            description = "Email token girildiğinde (Java4 gibi) ilgili sınıfa ait tüm trainerların adını ve soyadını listeleyen metot. #128",
            tags = {"Trainer Tag Controller"},
            parameters = {
                    @Parameter(name = "token", description = "Trainerları bulmak için kullanılacak email token'ı.", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Trainer listesi başarıyla alındı."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Geçersiz token."
                    )
            }
    )
    public List<UserSimpleResponseDto> findTrainersByEmailToken(@PathVariable String token){
        return trainerTagService.findTrainersByEmailToken(token);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-all")
    @Operation(
            summary = "Aktif Trainer Tag'leri Görüntüleme",
            description = "Aktif olan tüm trainer tag'lerin görüntülenmesini sağlayan metot. #129",
            tags = {"Trainer Tag Controller"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Aktif olan tüm trainer tag'ler başarıyla alındı."
                    )
            }
    )
    public ResponseEntity<List<TagResponseDto>> findAll() {
        List<TagResponseDto> responseDtoList = trainerTagService.findAll();
        return ResponseEntity.ok(responseDtoList);
    }

}
