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
    @Operation(summary = "Master trainer'ları görüntülemeyi sağlayan metot.")
    ResponseEntity<List<MasterTrainerResponseDto>> getMasterTrainerList() {
        return ResponseEntity.ok(trainerService.getMasterTrainerList());
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/assistant-trainers")
    @Operation(summary = "Asistant trainer'ları görüntülemeyi sağlayan metot.")
    ResponseEntity<List<AssistantTrainerResponseDto>> getAssistantTrainerList() {
        return ResponseEntity.ok(trainerService.getAssistantTrainerList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-all-trainers")
    @Operation(summary = "Tüm trainer'ları görüntülemeyi sağlayan metot.")
    ResponseEntity<List<TrainerResponseListTagsDto>> getTrainerList() {
        return ResponseEntity.ok(trainerService.getTrainerList());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/assign-trainer-tag")
    @Operation(summary = "Trainer tag oid ve trainer oid girilerek öğrencileri bir sınıfa kaydetmeyi sağlayan metot. #7")
    public ResponseEntity<TrainerResponseDto> updateTrainer(@RequestBody TrainerUpdateDto dto){
        return ResponseEntity.ok(trainerService.updateTrainer(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete-trainer-by-id/{id}")
    @Operation(summary = "Integer türünde id girilerek bulunan trainerın silinmesini sağlayan metot.")
    public ResponseEntity<Boolean> deleteTrainerById(@PathVariable Long id) {
        return ResponseEntity.ok(trainerService.deleteByTrainerOid(id));
    }

    @GetMapping("findUserByTrainerOid/{oid}")
    public ResponseEntity<User> findUserByTrainerOId(@PathVariable Long oid) {
        return ResponseEntity.ok(trainerService.findUserByTrainerOid(oid));
    }
}
