package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.StudentUpdateDto;
import com.bilgeadam.basurveyapp.dto.response.StudentResponseDto;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(
            summary = "Tüm Öğrencilerin Listelenmesi",
            description = "Tüm öğrencilerin listelenmesini sağlar. #81",
            tags = {"Student Controller"}
    )
    ResponseEntity<List<StudentResponseDto>> getStudentList() {
        return ResponseEntity.ok(studentService.getStudentList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/assign-student-tag")
    @Operation(
            summary = "Öğrenciye Etiket(Sınıf) Atama",
            description = "Belirtilen öğrenciye etiket(Sınıf) atama işlemini gerçekleştirir. #82",
            tags = {"Student Controller"}
    )
    public ResponseEntity<StudentResponseDto> updateStudent(@RequestBody StudentUpdateDto dto){
        return ResponseEntity.ok(studentService.updateStudent(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete-student-by-id/{id}")
    @Operation(
            summary = "Öğrenci Silme",
            description = "Belirtilen öğrenciyi siler. #83",
            tags = {"Student Controller"},
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Student ID",
                            required = true
                    )
            }
    )
    public ResponseEntity<Boolean> deleteStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.deleteByStudentOid(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("find-user-by-student-oid/{oid}")
    @Operation(
            summary = "Öğrenci Kullanıcısını Bul",
            description = "Öğrenci OID'sine göre öğrenci kullanıcısını bulur. #84",
            tags = {"Student Controller"},
            parameters = {
                    @Parameter(
                            name = "oid",
                            description = "Student oid",
                            required = true
                    )
            }
    )
    public ResponseEntity<User> findUserByStudentOId(@PathVariable Long oid) {
        return ResponseEntity.ok(studentService.findUserByStudentOid(oid));
    }
}
