package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.TrainerUpdateDto;
import com.bilgeadam.basurveyapp.dto.response.AssistantTrainerResponseDto;
import com.bilgeadam.basurveyapp.dto.response.MasterTrainerResponseDto;
import com.bilgeadam.basurveyapp.dto.response.TrainerResponseDto;
import com.bilgeadam.basurveyapp.entity.Trainer;
import com.bilgeadam.basurveyapp.services.TrainerService;
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
    @PostMapping("/create")
    public ResponseEntity<Boolean> createTrainer(@RequestBody Trainer trainer){
        return ResponseEntity.ok(trainerService.createTrainer(trainer));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/mastertrainers")
    ResponseEntity<List<MasterTrainerResponseDto>> getMasterTrainerList() {
        return ResponseEntity.ok(trainerService.getMasterTrainerList());
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/assistanttrainers")
    ResponseEntity<List<AssistantTrainerResponseDto>> getAssistantTrainerList() {
        return ResponseEntity.ok(trainerService.getAssistantTrainerList());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/signtoclass")
    public ResponseEntity<TrainerResponseDto> assignTrainerTag(@RequestBody TrainerUpdateDto dto){
        return ResponseEntity.ok(trainerService.updateTrainer(dto));
    }
}
