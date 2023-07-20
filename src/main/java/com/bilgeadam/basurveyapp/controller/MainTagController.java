package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateMainTagRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateTagDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateTagNameDto;
import com.bilgeadam.basurveyapp.dto.response.MainTagResponseDto;
import com.bilgeadam.basurveyapp.services.MainTagService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Muhammed Furkan Türkmen
 */
@RestController
@RequestMapping("/maintags")
@RequiredArgsConstructor
public class MainTagController {
    private final MainTagService mainTagService;
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "tag olusturma islemi. tag basliklarinda belirtilen isimde tag olusturur. tag class-> (QUESTION,STUDENT,SURVEY,TRAINER)")
    public ResponseEntity<Boolean> createMainTag(@RequestBody CreateMainTagRequestDto dto){
        mainTagService.createMainTag(dto);


        return ResponseEntity.ok(true);
    }

    @GetMapping("/tagname")
    @Operation(summary = "tag ismine göre arama")
    public ResponseEntity<List<MainTagResponseDto>> findByTagNames(@RequestParam String tagname){
        return ResponseEntity.ok(mainTagService.findByTagNames(tagname));
    }
    @GetMapping("/tagname/tagclass")
    @Operation(summary = "tag ismi ve tag classına göre arama.tag class-> (QUESTION,STUDENT,SURVEY,TRAINER)")
    public ResponseEntity<MainTagResponseDto> findByTagNameAndTagClass(@RequestParam String tagname,@RequestParam String tagclass){
        return ResponseEntity.ok(mainTagService.findByTagNameAndTagClass(tagname,tagclass));
    }
    @PutMapping("/tagname")
    @Operation(summary = "tag ismi ile update islemi yapılır.")
    public ResponseEntity<Boolean> updateTagByTagName(@RequestBody UpdateTagNameDto dto){
        return ResponseEntity.ok(mainTagService.updateTagByTagName(dto));
    }

    @PutMapping("/tagname/tagclass")
    @Operation(summary = "tag ismi ve tag classi ile update islemi yapılır.")
    public ResponseEntity<Boolean> updateTagByTagNameAndTagClass(@RequestBody UpdateTagDto dto){
        return ResponseEntity.ok(mainTagService.updateTagByTagNameAndTagClass(dto));
    }
    @GetMapping("/findall")
    @Operation(summary = "tüm tag'leri getirir")
    public ResponseEntity<List<MainTagResponseDto>> findAllTags(){
        return ResponseEntity.ok(mainTagService.findAllTags());
    }


}
