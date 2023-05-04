package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.StudentUpdateDto;
import com.bilgeadam.basurveyapp.dto.response.StudentResponseDto;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.mapper.StudentMapper;
import com.bilgeadam.basurveyapp.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    /*
        "/create" Method is summoned via StudentService Class in different services. There are no reasons to be able to access
        to this endpoint. Just in case of need, I leave it in comment line instead of deleting it.
     */
    /*
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<Boolean> createStudent(@RequestBody Student student){
        return ResponseEntity.ok(studentService.createStudent(student));
    }
     */

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/assignStudentTag")
    @Operation(summary = "")
    public ResponseEntity<StudentResponseDto> updateStudent(@RequestBody StudentUpdateDto dto){
        return ResponseEntity.ok(studentService.updateStudent(dto));
    }

}
