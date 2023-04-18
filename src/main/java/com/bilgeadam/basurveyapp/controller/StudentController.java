package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.StudentUpdateDto;
import com.bilgeadam.basurveyapp.dto.response.StudentResponseDto;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.mapper.StudentMapper;
import com.bilgeadam.basurveyapp.services.StudentService;
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
    ResponseEntity<List<StudentResponseDto>> getStudentList() {
        return ResponseEntity.ok(studentService.getStudentList());

    }
    /*
    Method is only summoned via StudentService, I could not find any valid reason to run it from
    controller. Just in case, I leave the method in comment line.
     */
/*

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<Boolean> createStudent(@RequestBody Student student){
        return ResponseEntity.ok(studentService.createStudent(student));
    }

 */
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/signtoclass")
    public ResponseEntity<StudentResponseDto> updateStudent(@RequestBody StudentUpdateDto dto){
        return ResponseEntity.ok(studentService.updateStudent(dto));
    }

}
