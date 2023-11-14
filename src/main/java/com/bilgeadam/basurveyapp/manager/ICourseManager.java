package com.bilgeadam.basurveyapp.manager;

import com.bilgeadam.basurveyapp.dto.response.CourseModalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(url = "http://localhost:8081/course",name = "api-course")
public interface ICourseManager {
    @GetMapping("/findAll")
    ResponseEntity<List<CourseModalResponse>> findAll();
}
