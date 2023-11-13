package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateCourseGroupRequestDto;
import com.bilgeadam.basurveyapp.dto.response.CourseGroupModelResponse2Dto;
import com.bilgeadam.basurveyapp.dto.response.CourseGroupModelResponseDto;
import com.bilgeadam.basurveyapp.dto.response.MessageResponseDto;
import com.bilgeadam.basurveyapp.entity.CourseGroup;
import com.bilgeadam.basurveyapp.repositories.ICourseGroupRepository;
import com.bilgeadam.basurveyapp.services.CourseGroupService;
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
    public ResponseEntity<List<CourseGroupModelResponseDto>> getAllDataFromCourseGroup(){
        return ResponseEntity.ok(courseGroupService.getAllDataFromCourseGroup());
    }

    @PostMapping("/create-course-group")
    public ResponseEntity<MessageResponseDto> createCourseGroup(@RequestBody @Valid CreateCourseGroupRequestDto dto){
        return ResponseEntity.ok(courseGroupService.createCourseGroup(dto));
    }

    @GetMapping("/find-all-course-group")
    public ResponseEntity<List<CourseGroup>> findall(){
        return ResponseEntity.ok(courseGroupService.findAllCourseGroup());
    }

    @GetMapping("/find-groups-by-name")
    public ResponseEntity<List<CourseGroup>> findByGroupName(@RequestParam String name){
        return ResponseEntity.ok(courseGroupService.findByGroupName(name));
    }

    @GetMapping("/find-course-group-by-course-id")
    public ResponseEntity<List<CourseGroup>> findCourseGroupByCourseId(@RequestParam Long courseId){
        return ResponseEntity.ok(courseGroupService.findCourseGroupByCourseId(courseId));
    }

    @GetMapping("/find-course-group-by-branch-id")
    public ResponseEntity<List<CourseGroup>> findCourseGroupByBranchId(@RequestParam Long branchId){
        return ResponseEntity.ok(courseGroupService.findCourseGroupByBranchId(branchId));
    }

    @GetMapping("/find-course-by-oid")
    public ResponseEntity<CourseGroup> findByCourseGroupOid(@RequestParam Long oid){
        return ResponseEntity.ok(courseGroupRepository.findById(oid).get());
    }

    @GetMapping("/find-course-group-by-trainer-id")
    public ResponseEntity<List<CourseGroup>> findCourseGroupByTrainerId(@RequestParam Long trainerId){
        return ResponseEntity.ok(courseGroupService.findCourseGroupByTrainerId(trainerId));
    }

    @GetMapping("/get-all-data-for-fronted-table")
    public ResponseEntity<List<CourseGroupModelResponse2Dto>> getAllDataForFrontendTable(){
        return ResponseEntity.ok(courseGroupService.getAllDataForFrontendTable());
    }
}
