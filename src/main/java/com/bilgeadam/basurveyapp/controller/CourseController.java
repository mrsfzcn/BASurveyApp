package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateCourseRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateBranchRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateCourseRequestDto;
import com.bilgeadam.basurveyapp.dto.response.CourseResponseDto;
import com.bilgeadam.basurveyapp.dto.response.MessageResponseDto;
import com.bilgeadam.basurveyapp.entity.Branch;
import com.bilgeadam.basurveyapp.entity.Course;
import com.bilgeadam.basurveyapp.services.CourseService;
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
    public ResponseEntity<CourseResponseDto> create(@RequestBody @Valid CreateCourseRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/soft-delete-by/{oid}")
    public ResponseEntity<Boolean> deleteCourseByOid(@PathVariable Long oid) {
        return ResponseEntity.ok(service.deleteCourseByOid(oid));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-by-name/{name}")
    public ResponseEntity<Course> findCoursesByName(@PathVariable String name) {
        return ResponseEntity.ok(service.findCoursesByName(name));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-by-apiId/{apiId}")
    public ResponseEntity<Course> findByApiId(@PathVariable String apiId) {
        return ResponseEntity.ok(service.findByApiId(apiId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update")
    public ResponseEntity<MessageResponseDto> updateCourseByApiId(@RequestBody @Valid UpdateCourseRequestDto dto) {
        return ResponseEntity.ok(service.updateCourseByApiId(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/active-course")
    public ResponseEntity<List<Course>> activeCourses(){
        return ResponseEntity.ok(service.findAllActiveCourses());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/deleted-courses")
    public ResponseEntity<List<Course>> deletedCourses() {
        return ResponseEntity.ok(service.findAllDeletedCourses());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/activate-course/{oid}")
    public ResponseEntity<MessageResponseDto> activateCourse(@PathVariable String id) {
        return ResponseEntity.ok(service.activateCourse(id));
    }

    //qwe
}
