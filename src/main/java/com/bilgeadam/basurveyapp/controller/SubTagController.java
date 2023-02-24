package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateSubtagDto;
import com.bilgeadam.basurveyapp.dto.response.SubtagResponseDto;
import com.bilgeadam.basurveyapp.services.SubTagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subtag")
@RequiredArgsConstructor
public class SubTagController {
    private final SubTagService subtagService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<Void> createsubTag(@RequestBody @Valid CreateSubtagDto dto) {
        subtagService.createSubTag(dto);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findall")
    public ResponseEntity<List<SubtagResponseDto>> findAllSubtag() {
        List<SubtagResponseDto> responseDtoList = subtagService.findAll();
        return ResponseEntity.ok(responseDtoList);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody @Valid Long subTagStringId) {
        return ResponseEntity.ok(subtagService.delete(subTagStringId));
    }
}
