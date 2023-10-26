package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.StudentUpdateDto;
import com.bilgeadam.basurveyapp.dto.response.StudentResponseDto;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/students")
    @Operation(summary = "Tüm student'ların listelenmesini sağlayan metot.")
    ResponseEntity<List<StudentResponseDto>> getStudentList() {
        return ResponseEntity.ok(studentService.getStudentList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/assign-student-tag")
    @Operation(summary = "#6")
    public ResponseEntity<StudentResponseDto> updateStudent(@RequestBody StudentUpdateDto dto){
        return ResponseEntity.ok(studentService.updateStudent(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete-student-by-id/{id}")
    @Operation(summary = "Integer türünde id girilerek bulunan student'ın silinmesini sağlayan metot.")
    public ResponseEntity<Boolean> deleteStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.deleteByStudentOid(id));
    }

    @GetMapping("findUserByStudentOid/{oid}")
    public ResponseEntity<User> findUserByStudentOId(@PathVariable Long oid) {
        return ResponseEntity.ok(studentService.findUserByStudentOid(oid));
    }
}
