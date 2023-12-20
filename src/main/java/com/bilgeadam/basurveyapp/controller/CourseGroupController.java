package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateCourseGroupRequestDto;
import com.bilgeadam.basurveyapp.dto.response.CourseGroupModelResponse2Dto;
import com.bilgeadam.basurveyapp.dto.response.CourseGroupModelResponseDto;
import com.bilgeadam.basurveyapp.dto.response.MessageResponseDto;
import com.bilgeadam.basurveyapp.entity.CourseGroup;
import com.bilgeadam.basurveyapp.repositories.ICourseGroupRepository;
import com.bilgeadam.basurveyapp.services.CourseGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course-group")
public class CourseGroupController {
    private final CourseGroupService courseGroupService;
    private final ICourseGroupRepository courseGroupRepository;

    @GetMapping("/get-all-data-from-course-group")
    @Operation(
            summary = "(Deprecated) Tüm Kurs Grubu Verilerini Getirme",
            description = "Sistemdeki tüm kurs gruplarına ait verileri getiren metot. #26",
            tags = {"Course Group Controller"}
    )
    public ResponseEntity<List<CourseGroupModelResponseDto>> getAllDataFromCourseGroup(){
        return ResponseEntity.ok(courseGroupService.getAllDataFromCourseGroup());
    }

    @PostMapping("/create-course-group")
    @Operation(
            summary = "Kurs Grubu Oluşturma",
            description = "Yeni bir kurs grubu oluşturan metot. Verilen bilgilere göre bir kurs grubu oluşturulur. #27",
            tags = {"Course Group Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Oluşturulacak kurs grubu bilgilerini içeren istek gövdesi. apiId-name-startDate-endDate-courseId-branchId-trainers(List)",
                    required = true
            )
    )
    public ResponseEntity<MessageResponseDto> createCourseGroup(@RequestBody @Valid CreateCourseGroupRequestDto dto){
        return ResponseEntity.ok(courseGroupService.createCourseGroup(dto));
    }

    @GetMapping("/find-all-course-group")
    @Operation(
            summary = "Tüm Kurs Gruplarını Listeleme",
            description = "Sistemdeki tüm kurs gruplarını listeleyen metot. #28",
            tags = {"Course Group Controller"}
    )
    public ResponseEntity<List<CourseGroup>> findall(){
        return ResponseEntity.ok(courseGroupService.findAllCourseGroup());
    }

    @GetMapping("/find-groups-by-name")
    @Operation(
            summary = "İsimle Kurs Gruplarını Bulma",
            description = "Belirtilen isimle kurs gruplarını bulan metot. #29",
            tags = {"Course Group Controller"},
            parameters = {
                    @Parameter(name = "name", description = "Bulunacak kurs gruplarının adı", required = true)
            }
    )
    public ResponseEntity<List<CourseGroup>> findByGroupName(@RequestParam String name){
        return ResponseEntity.ok(courseGroupService.findByGroupName(name));
    }

    @GetMapping("/find-course-group-by-course-id")
    @Operation(
            summary = "Kurs ID'ye Göre Kurs Gruplarını Bulma",
            description = "Belirtilen kurs ID'sine sahip kurs gruplarını bulan metot. #30",
            tags = {"Course Group Controller"},
            parameters = {
                    @Parameter(name = "courseId", description = "Bulunacak kurs gruplarının kurs ID'si", required = true)
            }
    )
    public ResponseEntity<List<CourseGroup>> findCourseGroupByCourseId(@RequestParam Long courseId){
        return ResponseEntity.ok(courseGroupService.findCourseGroupByCourseId(courseId));
    }

    @GetMapping("/find-course-group-by-branch-id")
    @Operation(
            summary = "Şube ID'ye Göre Kurs Gruplarını Bulma",
            description = "Belirtilen şube ID'sine sahip kurs gruplarını bulan metot. #31",
            tags = {"Course Group Controller"},
            parameters = {
                    @Parameter(name = "branchId", description = "Bulunacak kurs gruplarının şube ID'si", required = true)
            }
    )
    public ResponseEntity<List<CourseGroup>> findCourseGroupByBranchId(@RequestParam Long branchId){
        return ResponseEntity.ok(courseGroupService.findCourseGroupByBranchId(branchId));
    }

    @GetMapping("/find-course-by-oid")
    @Operation(
            summary = "Kurs Grubu OID'ye Göre Bulma",
            description = "Belirtilen Kurs Grubu OID'ye sahip kurs grubunu bulan metot. #32",
            tags = {"Course Group Controller"},
            parameters = {
                    @Parameter(name = "oid", description = "Bulunacak kurs grubunun OID değeri", required = true)
            }
    )
    public ResponseEntity<CourseGroup> findByCourseGroupOid(@RequestParam Long oid){
        return ResponseEntity.ok(courseGroupRepository.findById(oid).get());
    }

    @GetMapping("/find-course-group-by-trainer-id")
    @Operation(
            summary = "Eğitmen ID'ye Göre Kurs Gruplarını Bulma",
            description = "Belirtilen eğitmen ID'sine sahip kurs gruplarını bulan metot. #33",
            tags = {"Course Group Controller"},
            parameters = {
                    @Parameter(name = "trainerId", description = "Bulunacak kurs gruplarının eğitmen ID'si", required = true)
            }
    )
    public ResponseEntity<List<CourseGroup>> findCourseGroupByTrainerId(@RequestParam Long trainerId){
        return ResponseEntity.ok(courseGroupService.findCourseGroupByTrainerId(trainerId));
    }

    @GetMapping("/get-all-data-for-fronted-table")
    @Operation(
            summary = "Tüm Verileri Getirme",
            description = "Frontend tablo için gerekli verileri getiren metot. #34",
            tags = {"Course Group Controller"}
    )
    public ResponseEntity<List<CourseGroupModelResponse2Dto>> getAllDataForFrontendTable(){
        return ResponseEntity.ok(courseGroupService.getAllDataForFrontendTable());
    }
}
