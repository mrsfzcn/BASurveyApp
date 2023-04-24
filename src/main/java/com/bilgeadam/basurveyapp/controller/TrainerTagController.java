package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.request.FindTrainersByTagStringRequestDto;
import com.bilgeadam.basurveyapp.dto.request.FindUserByEmailRequestDto;
import com.bilgeadam.basurveyapp.dto.response.FindActiveTrainerTagByIdResponseDto;
import com.bilgeadam.basurveyapp.dto.response.GetTrainerTagsByEmailResponse;
import com.bilgeadam.basurveyapp.dto.response.TrainerTagDetailResponseDto;
import com.bilgeadam.basurveyapp.services.TrainerTagService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainertag")
@RequiredArgsConstructor
public class TrainerTagController {
    private final TrainerTagService trainerTagService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    @Operation(summary = "java2, .net vb. gibi trainer tag oluşturmamızı sağlayan metot.(exception ı doğru çalışıyor.) ")
    public ResponseEntity<String> createTag(@RequestBody @Valid CreateTagDto dto ){
        trainerTagService.createTag(dto);
        return ResponseEntity.ok(dto.getTagString());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/findActiveById")
    @Operation(summary = "girilen oid nin statusu aktifse bu tagin tagstringini(java4, .net2 gibi) dönen metot(exceptionı düzeltildi.))")
    public ResponseEntity<FindActiveTrainerTagByIdResponseDto> findActiveById(@RequestBody @Valid Long trainerTagOid ){
        return ResponseEntity.ok(trainerTagService.findActiveById(trainerTagOid));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping ("/delete")
    @Operation(summary = "verilen trainer tag oid mevcutsa ve statusu aktifse statusunu deleted e çeken metot (exceptionı düzeltildi.)")
    public ResponseEntity<Boolean> delete(@RequestBody @Valid Long trainerTagOid ){
        return ResponseEntity.ok(trainerTagService.delete(trainerTagOid));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/trainertags")
    @Operation(summary = "trainer tag stringlerini(.net2,java1,java3 gibi) ve trainer tag oid lerini dönen metot.")
    public ResponseEntity<List<TrainerTagDetailResponseDto>> getTrainerTagList(){
        return ResponseEntity.ok(trainerTagService.getTrainerTagList());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/findrainerstagsbyemail")
    @Operation(summary = "trainer emaili girildiğinde ilgili trainera ait tüm tag stringlerini(java1,java2.. gibi) dönen metot.")
    public ResponseEntity<GetTrainerTagsByEmailResponse> getTrainerTagsByEmail(@RequestBody @Valid FindUserByEmailRequestDto dto){
        return ResponseEntity.ok(trainerTagService.getTrainerTagsByEmail(dto.getEmail()));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/findtrainersbytrainertag")
    @Operation(summary = "trainer tag string i girildiğinde (Java4 gibi) ilgili sınıfa ait tüm trainerların adını ve soyadını listeleyen metot")
    public List<String> findTrainersByTrainerTagString(@RequestBody @Valid FindTrainersByTagStringRequestDto dto){
        return trainerTagService.findTrainersByTrainerTagString(dto.getTagString());
    }

}
