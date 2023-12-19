package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.TrainerUpdateDto;
import com.bilgeadam.basurveyapp.dto.response.AssistantTrainerResponseDto;
import com.bilgeadam.basurveyapp.dto.response.MasterTrainerResponseDto;
import com.bilgeadam.basurveyapp.dto.response.TrainerResponseDto;
import com.bilgeadam.basurveyapp.dto.response.TrainerResponseListTagsDto;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
import com.bilgeadam.basurveyapp.services.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainer")
public class TrainerController {
    private final TrainerService trainerService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/master-trainers")
    @Operation(
            summary = "Master Trainer Listesini Getir",
            description = "Master trainer'ları görüntülemeyi sağlayan metot. #117",
            tags = {"Trainer Controller"}
    )
    ResponseEntity<List<MasterTrainerResponseDto>> getMasterTrainerList() {
        return ResponseEntity.ok(trainerService.getMasterTrainerList());
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/assistant-trainers")
    @Operation(
            summary = "Assistant Trainer Listesini Getir",
            description = "Assistant trainer'ları görüntülemeyi sağlayan metot. #118",
            tags = {"Trainer Controller"}
    )
    ResponseEntity<List<AssistantTrainerResponseDto>> getAssistantTrainerList() {
        return ResponseEntity.ok(trainerService.getAssistantTrainerList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-all-trainers")
    @Operation(
            summary = "Trainer Listesini Getir",
            description = "Tüm trainer'ları görüntülemeyi sağlayan metot. #119",
            tags = {"Trainer Controller"}
    )
    ResponseEntity<List<TrainerResponseListTagsDto>> getTrainerList() {
        return ResponseEntity.ok(trainerService.getTrainerList());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/assign-trainer-tag")
    @Operation(
            summary = "Trainer'a Tag Ata",
            description = "Trainer tag oid ve trainer oid girilerek öğrencileri bir sınıfa kaydetmeyi sağlayan metot. #120",
            tags = {"Trainer Controller"}
    )
    public ResponseEntity<TrainerResponseDto> updateTrainer(@RequestBody TrainerUpdateDto dto){
        return ResponseEntity.ok(trainerService.updateTrainer(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete-trainer-by-id/{id}")
    @Operation(
            summary = "Trainer Silme",
            description = "Integer türünde id girilerek bulunan trainer'ın silinmesini sağlayan metot. #121",
            tags = {"Trainer Controller"}
    )
    public ResponseEntity<Boolean> deleteTrainerById(@PathVariable Long id) {
        return ResponseEntity.ok(trainerService.deleteByTrainerOid(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("find-user-by-trainer-oid/{oid}")
    @Operation(
            summary = "Trainer'a Göre Kullanıcı Bulma",
            description = "Trainer'ın oid'sine göre kullanıcıyı bulan metot. #122",
            tags = {"Trainer Controller"},
            parameters = {
                    @Parameter(
                            name = "oid",
                            description = "Trainer Kimliği (Trainer OID)",
                            required = true
                    )
            }
    )
    public ResponseEntity<User> findUserByTrainerOId(@PathVariable Long oid) {
        return ResponseEntity.ok(trainerService.findUserByTrainerOid(oid));
    }
}
