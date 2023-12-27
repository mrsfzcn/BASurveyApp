package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.StudentUpdateDto;
import com.bilgeadam.basurveyapp.dto.response.StudentResponseDto;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
            tags = {"Student Controller"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Öğrenciler başarıyla alındı."
                    )
            }
    )
    ResponseEntity<List<StudentResponseDto>> getStudentList() {
        return ResponseEntity.ok(studentService.getStudentList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/assign-student-tag")
    @Operation(
            summary = "Öğrenciye Etiket(Sınıf) Atama",
            description = "Belirtilen öğrenciye etiket(Sınıf) atama işlemini gerçekleştirir. #82",
            tags = {"Student Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Atanacak etiket bilgisini içeren istek gövdesi. studentTagOid-studentOid",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Etiket(Sınıf) başarıyla öğrenciye atanmıştır."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Kaynak bulunamadı."
                    )
            }
    )
    public ResponseEntity<StudentResponseDto> updateStudent(@RequestBody StudentUpdateDto dto) {
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
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Öğrenci başarıyla silinmiştir."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Kaynak bulunamadı."
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
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Öğrenci kullanıcısı başarıyla bulundu."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Kaynak bulunamadı."
                    )
            }
    )
    public ResponseEntity<User> findUserByStudentOId(@PathVariable Long oid) {
        return ResponseEntity.ok(studentService.findUserByStudentOid(oid));
    }

    @PostMapping("/find-by-courseId/{course_group_oid}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Course id ile öğrenci döner ve öğrenci tablosundaki user id ile ilgili user bilgilerini döner", tags = {"Student Controller"}, parameters = {
            @Parameter(
                    name = "course_group_oid",
                    description = "course_group oid",
                    required = true
            )
    },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sınıfa göre user başarıyla bulundu."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Kaynak bulunamadı."
                    )
            })
    public ResponseEntity<List<User>> findUserByCourseId(@PathVariable Long course_group_oid) {
        return ResponseEntity.ok(studentService.findByCourseId(course_group_oid));
    }
}
