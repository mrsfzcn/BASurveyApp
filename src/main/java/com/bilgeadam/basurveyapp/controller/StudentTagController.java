package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.response.StudentResponseDto;
import com.bilgeadam.basurveyapp.dto.response.StudentTagDetailResponseDto;
import com.bilgeadam.basurveyapp.dto.response.StudentTagResponseDto;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.services.StudentTagService;
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
    public ResponseEntity<String> createTag(@RequestBody @Valid CreateTagDto dto ){
        studentTagService.createTag(dto);
        return ResponseEntity.ok(dto.getTagString());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/getStudentsByStudentTag")
    public ResponseEntity<List<Student>> getStudentsByStudentTag(@RequestBody @Valid StudentTag studentTag ){
        return ResponseEntity.ok(studentTagService.getStudentsByStudentTag(studentTag));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/findByStudentTagName")
    public ResponseEntity<Optional<StudentTag>> findByStudentTagName(@RequestParam @Valid String studentTag ){
        return ResponseEntity.ok(studentTagService.findByStudentTagName(studentTag));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/findByStudentTagOid")
    public ResponseEntity<List<Student>> findByStudentTagOid(@RequestBody @Valid Long studentTagOid ){
        return ResponseEntity.ok(studentTagService.findByStudentTagOid(studentTagOid));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/findActiveById")
    public ResponseEntity<Optional<StudentTag>> findActiveById(@RequestBody @Valid Long studentTagOid ){
        return ResponseEntity.ok(studentTagService.findActiveById(studentTagOid));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody @Valid Long studentTagOid ){
        return ResponseEntity.ok(studentTagService.delete(studentTagOid));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/studenttags")
    ResponseEntity<List<StudentTagDetailResponseDto>> getStudentTagList() {
        return ResponseEntity.ok(studentTagService.getStudentTagList());
    }
}
