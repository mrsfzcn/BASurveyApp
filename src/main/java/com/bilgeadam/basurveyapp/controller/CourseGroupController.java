package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateCourseGroupRequestDto;
import com.bilgeadam.basurveyapp.dto.response.CourseGroupModelResponse;
import com.bilgeadam.basurveyapp.dto.response.MessageResponseDto;
import com.bilgeadam.basurveyapp.entity.CourseGroup;
import com.bilgeadam.basurveyapp.mapper.ICourseGroupMapper;
import com.bilgeadam.basurveyapp.repositories.ICourseGroupRepository;
import com.bilgeadam.basurveyapp.services.CourseGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course-group")
public class CourseGroupController {
    private final CourseGroupService courseGroupService;
    private final ICourseGroupRepository courseGroupRepository;

    @GetMapping("/get-all-data-from-course-group")
    public ResponseEntity<List<CourseGroupModelResponse>> getAllDataFromCourseGroup(){
        return ResponseEntity.ok(courseGroupService.getAllDataFromCourseGroup());
    }

    @PostMapping("/create-course-group")
    public ResponseEntity<MessageResponseDto> createCourseGroup(@RequestBody CreateCourseGroupRequestDto dto){
        return ResponseEntity.ok(courseGroupService.createCourseGroup(dto));
    }

    @GetMapping("/findallcoursegroup")
    public ResponseEntity<List<CourseGroup>> findall(){
        return ResponseEntity.ok(courseGroupService.findAllCourseGroup());
    }

    @GetMapping("/findgroupsbyname")
    public ResponseEntity<List<CourseGroup>> findByGroupName(String name){
        return ResponseEntity.ok(courseGroupService.findByGroupName(name));
    }

    @GetMapping("/findcoursegroupbycourseid")
    public ResponseEntity<List<CourseGroup>> findCourseGroupByCourseId(Long courseId){
        return ResponseEntity.ok(courseGroupService.findCourseGroupByCourseId(courseId));
    }

    @GetMapping("/findcoursegroupbybranchid")
    public ResponseEntity<List<CourseGroup>> findCourseGroupByBranchId(Long branchId){
        return ResponseEntity.ok(courseGroupService.findCourseGroupByBranchId(branchId));
    }

    @GetMapping("/findcoursebyoid")
    public ResponseEntity<CourseGroup> findByCourseGroupOid(Long oid){
        return ResponseEntity.ok(courseGroupRepository.findById(oid).get());
    }

    @GetMapping("/findcoursegroupbytrainerid")
    public ResponseEntity<List<CourseGroup>> findCourseGroupByTrainerId(Long trainerId){
        return ResponseEntity.ok(courseGroupService.findCourseGroupByTrainerId(trainerId));
    }

}
