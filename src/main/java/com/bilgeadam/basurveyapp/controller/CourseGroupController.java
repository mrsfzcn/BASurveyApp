package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateCourseGroupRequestDto;
import com.bilgeadam.basurveyapp.dto.response.CourseGroupModelResponse;
import com.bilgeadam.basurveyapp.dto.response.MessageResponseDto;
import com.bilgeadam.basurveyapp.services.CourseGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course-group")
public class CourseGroupController {
    private final CourseGroupService courseGroupService;

    @GetMapping("/get-all-data-from-course-group")
    public ResponseEntity<List<CourseGroupModelResponse>> getAllDataFromCourseGroup(){
        return ResponseEntity.ok(courseGroupService.getAllDataFromCourseGroup());
    }

    @PostMapping("/create-course-group")
    public ResponseEntity<MessageResponseDto> createCourseGroup(@RequestBody CreateCourseGroupRequestDto dto){
        return ResponseEntity.ok(courseGroupService.createCourseGroup(dto));
    }
}
