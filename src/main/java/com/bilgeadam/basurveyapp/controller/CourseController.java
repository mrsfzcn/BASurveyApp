package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateCourseRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateBranchRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateCourseRequestDto;
import com.bilgeadam.basurveyapp.dto.response.CourseResponseDto;
import com.bilgeadam.basurveyapp.dto.response.MessageResponseDto;
import com.bilgeadam.basurveyapp.entity.Branch;
import com.bilgeadam.basurveyapp.entity.Course;
import com.bilgeadam.basurveyapp.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {
    private final CourseService service;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    @Operation(
            summary = "Kurs Oluşturma",
            description = "Yeni bir kurs oluşturan metot. Verilen bilgilere göre bir kurs oluşturulur. #18",
            tags = {"Course Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Yeni kursun bilgilerini içeren istek gövdesi. apiId-name",
                    required = true
            )
    )
    public ResponseEntity<CourseResponseDto> create(@RequestBody @Valid CreateCourseRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/soft-delete-by/{oid}")
    @Operation(
            summary = "Kurs Soft Silme",
            description = "Belirtilen OID'ye sahip kursu soft olarak silen metot. Kurs zaten silinmişse hata fırlatılır. #19",
            tags = {"Course Controller"},
            parameters = {
                    @Parameter(name = "oid", description = "Silinecek kursun OID değeri", required = true)
            }
    )
    public ResponseEntity<Boolean> deleteCourseByOid(@PathVariable Long oid) {
        return ResponseEntity.ok(service.deleteCourseByOid(oid));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-by-name/{name}")
    @Operation(
            summary = "Kurs Adına Göre Bulma",
            description = "Belirtilen isimdeki kursu bulan metot. Eğer kurs bulunamazsa hata fırlatılır. #20",
            tags = {"Course Controller"},
            parameters = {
                    @Parameter(name = "name", description = "Bulunacak kursun adı", required = true)
            }
    )
    public ResponseEntity<Course> findCoursesByName(@PathVariable String name) {
        return ResponseEntity.ok(service.findCoursesByName(name));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-by-apiId/{apiId}")
    @Operation(
            summary = "Kurs API ID'ye Göre Bulma",
            description = "Belirtilen API ID'ye sahip kursu bulan metot. Eğer kurs bulunamazsa hata fırlatılır. #21",
            tags = {"Course Controller"},
            parameters = {
                    @Parameter(name = "apiId", description = "Bulunacak kursun API ID'si", required = true)
            }
    )
    public ResponseEntity<Course> findByApiId(@PathVariable String apiId) {
        return ResponseEntity.ok(service.findByApiId(apiId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update")
    @Operation(
            summary = "Kurs Güncelleme",
            description = "Belirtilen API ID'ye sahip kursu güncelleyen metot. Verilen bilgilere göre bir kurs güncellenir. #22",
            tags = {"Course Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Güncellenecek kurs bilgilerini içeren istek gövdesi. apiId-name",
                    required = true
            )
    )
    public ResponseEntity<MessageResponseDto> updateCourseByApiId(@RequestBody @Valid UpdateCourseRequestDto dto) {
        return ResponseEntity.ok(service.updateCourseByApiId(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/active-course")
    @Operation(
            summary = "Aktif Kursları Listeleme",
            description = "Sistemdeki tüm aktif kursları listeleyen metot. #23",
            tags = {"Course Controller"}
    )
    public ResponseEntity<List<Course>> activeCourses(){
        return ResponseEntity.ok(service.findAllActiveCourses());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/deleted-courses")
    @Operation(
            summary = "Silinmiş Kursları Listeleme",
            description = "Sistemdeki tüm silinmiş kursları listeleyen metot. #24",
            tags = {"Course Controller"}
    )
    public ResponseEntity<List<Course>> deletedCourses() {
        return ResponseEntity.ok(service.findAllDeletedCourses());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/activate-course/{oid}")
    @Operation(
            summary = "Kurs Aktivasyonu",
            description = "Belirtilen API ID'ye sahip kursu aktif hale getiren metot. #25",//id'lerle alakalı bir sıkıntı olabilir. Bir karışıklık var.
            tags = {"Course Controller"},
            parameters = {
                    @Parameter(name = "oid", description = "Aktifleştirilecek kursun API ID'si", required = true)
            }
    )
    public ResponseEntity<MessageResponseDto> activateCourse(@PathVariable String oid) {
        return ResponseEntity.ok(service.activateCourse(oid));
    }

    //qwe
}
