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
    @Operation(
            summary = "String girdisi ile yeni student tag oluştur #85",
            description = "Verilen string değeri kullanarak yeni bir student tag oluşturan metot.",
            tags = {"Student Tag Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Oluşturulacak student tag için string değeri içeren istek gövdesi. tagString-mainTagOid",
                    required = true
            )
    )
    @Hidden
    public ResponseEntity<StudentTagCreateResponseDto> createTag(@RequestBody @Valid CreateTagDto dto ){
        return ResponseEntity.ok(studentTagService.createTag(dto));

    }


    // kaldırılacak.
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/findByStudentTagName")
    @Operation(
            summary = "Öğrenci Etiketi İle Öğrenci Bulma",
            description = "Öğrenci etiketi kullanılarak bulunan öğrencinin bilgilerini gösteren metot. #86",
            hidden = true, // Not: Hidden annotation, Swagger'da bu endpoint'i gizler.
            tags = {"Student Tag Controller"}
    )
    @Hidden
    public ResponseEntity<Optional<StudentTag>> findByStudentTagName(@RequestParam @Valid String studentTag ){
        return ResponseEntity.ok(studentTagService.findByStudentTagName(studentTag));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/find-by-student-tag-oid")
    @Operation(
            summary = "Öğrenci Tag OID'sine Göre Öğrenci Bul",
            description = "Öğrenci Tag OID'sine göre öğrenciyi bulur. #87",
            tags = {"Student Controller"}
    )
    public ResponseEntity<List<Student>> findByStudentTagOid(@RequestBody @Valid Long studentTagOid ){
        return ResponseEntity.ok(studentTagService.findByStudentTagOid(studentTagOid));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/find-active-by-id")
    @Operation(
            summary = "Aktif Öğrenci Etiketini(sınıfını) Bul",
            description = "Belirtilen etiket(sınıf) OID'sine ait aktif öğrenci etiketini(sınıfını) getirir. #88",
            tags = {"Student Tag Controller"}
    )
    public ResponseEntity<Optional<StudentTag>> findActiveById(@RequestBody @Valid Long studentTagOid ){
        return ResponseEntity.ok(studentTagService.findActiveById(studentTagOid));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete")
    @Operation(
            summary = "Öğrenci Etiketini Sil",
            description = "Belirtilen öğrenci etiketi OID'sine ait öğrenciyi etiketten(sınıftan) siler. #89",
            tags = {"Student Tag Controller"}
    )
    public ResponseEntity<Boolean> delete(@RequestBody @Valid Long studentTagOid ){
        return ResponseEntity.ok(studentTagService.delete(studentTagOid));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/student-tags")
    @Operation(
            summary = "Tüm Öğrenci Etiketlerini Listele",
            description = "Sistemdeki tüm öğrenci etiketlerini(sınıflarını) listeler. #90",
            tags = {"Student Tag Controller"}
    )
    ResponseEntity<List<StudentTagDetailResponseDto>> getStudentTagList() {
        return ResponseEntity.ok(studentTagService.getStudentTagList());
    }
}
