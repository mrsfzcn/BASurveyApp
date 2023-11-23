package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.request.FindTrainersByTagStringRequestDto;
import com.bilgeadam.basurveyapp.dto.request.FindUserByEmailRequestDto;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.services.TrainerTagService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "java2, .net vb. gibi trainer tag oluşturmamızı sağlayan metot. #02 ")
    @Hidden
    public ResponseEntity<String> createTag(@RequestBody @Valid CreateTagDto dto ){
        trainerTagService.createTag(dto);
        return ResponseEntity.ok(dto.getTagString());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/find-active-by-id")
    @Operation(summary = "girilen oid nin statusu aktifse bu tagin tagstringini(java4, .net2 gibi) dönen metot)")
    public ResponseEntity<FindActiveTrainerTagByIdResponseDto> findActiveById(@RequestBody @Valid Long trainerTagOid ){
        return ResponseEntity.ok(trainerTagService.findActiveById(trainerTagOid));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping ("/delete")
    @Operation(summary = "verilen trainer tag oid mevcutsa ve statusu aktifse statusunu deleted e çeken metot ")
    public ResponseEntity<Boolean> delete(@RequestBody @Valid Long trainerTagOid ){
        return ResponseEntity.ok(trainerTagService.delete(trainerTagOid));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/trainer-tags")
    @Operation(summary = "trainer tag stringlerini(.net2,java1,java3 gibi) ve trainer tag oid lerini dönen metot.")
    public ResponseEntity<List<TrainerTagDetailResponseDto>> getTrainerTagList(){
        return ResponseEntity.ok(trainerTagService.getTrainerTagList());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/find-trainers-tags-by-email")
    @Operation(summary = "trainer emaili girildiğinde ilgili trainera ait tüm tag stringlerini(java1,java2.. gibi) dönen metot.")
    public ResponseEntity<GetTrainerTagsByEmailResponse> getTrainerTagsByEmail(@RequestBody @Valid FindUserByEmailRequestDto dto){
        return ResponseEntity.ok(trainerTagService.getTrainerTagsByEmail(dto.getEmail()));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','STUDENT')")
    @GetMapping("/find-trainers-by-trainer-tag/{token}")
    @Operation(summary = "Email token girildiğinde (Java4 gibi) ilgili sınıfa ait tüm trainerların adını ve soyadını listeleyen metot")
    public List<UserSimpleResponseDto> findTrainersByEmailToken(@PathVariable String token){
        return trainerTagService.findTrainersByEmailToken(token);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-all")
    @Operation(summary = "Aktif olan tüm trainer tag'lerin görüntülenmesini sağlayan metot.")
    public ResponseEntity<List<TagResponseDto>> findAll() {
        List<TagResponseDto> responseDtoList = trainerTagService.findAll();
        return ResponseEntity.ok(responseDtoList);
    }

}
