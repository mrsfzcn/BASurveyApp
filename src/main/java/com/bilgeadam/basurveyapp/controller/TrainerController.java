package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.TrainerUpdateDto;
import com.bilgeadam.basurveyapp.dto.response.AssistantTrainerResponseDto;
import com.bilgeadam.basurveyapp.dto.response.MasterTrainerResponseDto;
import com.bilgeadam.basurveyapp.dto.response.TrainerResponseDto;
import com.bilgeadam.basurveyapp.services.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainer")
public class TrainerController {
    private final TrainerService trainerService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/mastertrainers")
    @Operation(summary = "Master trainer'ları görüntülemeyi sağlayan metot.")
    ResponseEntity<List<MasterTrainerResponseDto>> getMasterTrainerList() {
        return ResponseEntity.ok(trainerService.getMasterTrainerList());
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/assistanttrainers")
    @Operation(summary = "Asistant trainer'ları görüntülemeyi sağlayan metot.")
    ResponseEntity<List<AssistantTrainerResponseDto>> getAssistantTrainerList() {
        return ResponseEntity.ok(trainerService.getAssistantTrainerList());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/assignTrainerTag")
    @Operation(summary = "Trainer tag oid ve trainer oid girilerek öğrencileri bir sınıfa kaydetmeyi sağlayan metot. #7")
    public ResponseEntity<TrainerResponseDto> assignTrainerTag(@RequestBody TrainerUpdateDto dto){
        return ResponseEntity.ok(trainerService.updateTrainer(dto));
    }
}
