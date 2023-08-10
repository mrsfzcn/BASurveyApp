package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.response.StudentTagCreateResponseDto;
import com.bilgeadam.basurveyapp.dto.response.StudentTagDetailResponseDto;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.services.StudentTagService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student-tag")
@RequiredArgsConstructor
public class StudentTagController {

    private final StudentTagService studentTagService;
    // kaldırılacak.
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/")
    @Operation(summary = "String girdisi ile yeni student tag oluşturulmasını sağlayan metot. #03")
    @Hidden
    public ResponseEntity<StudentTagCreateResponseDto> createTag(@RequestBody @Valid CreateTagDto dto ){
        return ResponseEntity.ok(studentTagService.createTag(dto));

    }


    // kaldırılacak.
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/findByStudentTagName")
    @Operation(summary = "student tag girilerek bulunan student'ın gösterilmesini sağlayan metot.")
    @Hidden
    public ResponseEntity<Optional<StudentTag>> findByStudentTagName(@RequestParam @Valid String studentTag ){
        return ResponseEntity.ok(studentTagService.findByStudentTagName(studentTag));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/find-by-student-tag-oid")
    @Operation(summary = "student tag oid girerek ulaşılan student'ın gösterilmesini sağlayan metot.")
    public ResponseEntity<List<Student>> findByStudentTagOid(@RequestBody @Valid Long studentTagOid ){
        return ResponseEntity.ok(studentTagService.findByStudentTagOid(studentTagOid));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/find-active-by-id")
    @Operation(summary = "studentTagOid ile ilgili oid'ye ait StudentTag'i getiriyor. (ACTIVE ise)")
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
    @GetMapping("/student-tags")
    @Operation(summary = "Tüm student tag'lerin görüntülenmesini sağlayan metot.")
    ResponseEntity<List<StudentTagDetailResponseDto>> getStudentTagList() {
        return ResponseEntity.ok(studentTagService.getStudentTagList());
    }
}
