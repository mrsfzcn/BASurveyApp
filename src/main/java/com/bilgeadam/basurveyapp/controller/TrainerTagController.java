package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.response.TrainerTagDetailResponseDto;
import com.bilgeadam.basurveyapp.entity.Trainer;
import com.bilgeadam.basurveyapp.entity.tags.TrainerTag;
import com.bilgeadam.basurveyapp.services.TrainerTagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/trainertag")
@RequiredArgsConstructor
public class TrainerTagController {
    private final TrainerTagService trainerTagService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<String> createTag(@RequestBody @Valid CreateTagDto dto ){
        trainerTagService.createTag(dto);
        return ResponseEntity.ok(dto.getTagString());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/getTrainerTagsOids")
    public ResponseEntity<Set<Long>> getTrainerTagsOids(@RequestBody @Valid Trainer trainer ){
        return ResponseEntity.ok(trainerTagService.getTrainerTagsOids(trainer));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/findActiveById")
    public ResponseEntity<Optional<TrainerTag>> findActiveById(@RequestBody @Valid Long trainerTagOid ){
        return ResponseEntity.ok(trainerTagService.findActiveById(trainerTagOid));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/getTrainerTags")
    public ResponseEntity<Set<TrainerTag>> getTrainerTags(@RequestBody @Valid Trainer trainer ){
        return ResponseEntity.ok(trainerTagService.getTrainerTags(trainer));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping ("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody @Valid Long trainerTagOid ){
        return ResponseEntity.ok(trainerTagService.delete(trainerTagOid));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/trainertags")
    public ResponseEntity<List<TrainerTagDetailResponseDto>> getTrainerTagList(){
        return ResponseEntity.ok(trainerTagService.getTrainerTagList());
    }
}
