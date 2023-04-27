package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.response.StudentResponseDto;
import com.bilgeadam.basurveyapp.dto.response.StudentTagDetailResponseDto;
import com.bilgeadam.basurveyapp.dto.response.StudentTagResponseDto;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.services.StudentTagService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/studenttag")
@RequiredArgsConstructor
public class StudentTagController {

    private final StudentTagService studentTagService;
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    @Operation(summary = "String girdisi ile yeni student tag oluşturulmasını sağlayan metot.")
    public ResponseEntity<String> createTag(@RequestBody @Valid CreateTagDto dto ){
        studentTagService.createTag(dto);
        return ResponseEntity.ok(dto.getTagString());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/getStudentsByStudentTag")
    @Operation(summary = "")
    public ResponseEntity<List<Student>> getStudentsByStudentTag(@RequestBody @Valid StudentTag studentTag ){
        return ResponseEntity.ok(studentTagService.getStudentsByStudentTag(studentTag));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/findByStudentTagName")
    @Operation(summary = "student tag girilerek bulunan student'ın gösterilmesini sağlayan metot.")
    public ResponseEntity<Optional<StudentTag>> findByStudentTagName(@RequestParam @Valid String studentTag ){
        return ResponseEntity.ok(studentTagService.findByStudentTagName(studentTag));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/findByStudentTagOid")
    @Operation(summary = "student tag oid girerek ulaşılan student'ın gösterilmesini sağlayan metot.")
    public ResponseEntity<List<Student>> findByStudentTagOid(@RequestBody @Valid Long studentTagOid ){
        return ResponseEntity.ok(studentTagService.findByStudentTagOid(studentTagOid));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/findActiveById")
    @Operation(summary = "student tag oid girilerek active durumdaki student'ların gösterilmesini sağlayan metot.")
    public ResponseEntity<Optional<StudentTag>> findActiveById(@RequestBody @Valid Long studentTagOid ){
        return ResponseEntity.ok(studentTagService.findActiveById(studentTagOid));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete")
    @Operation(summary = "student tag oid girilerek ulaşılan student'in silinmesinği sağlayan metot.")
    public ResponseEntity<Boolean> delete(@RequestBody @Valid Long studentTagOid ){
        return ResponseEntity.ok(studentTagService.delete(studentTagOid));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/studenttags")
    @Operation(summary = "Tüm student tag'lerin görüntülenmesini sağlayan metot.")
    ResponseEntity<List<StudentTagDetailResponseDto>> getStudentTagList() {
        return ResponseEntity.ok(studentTagService.getStudentTagList());
    }
}
