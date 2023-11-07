package com.bilgeadam.basurveyapp.manager;

import com.bilgeadam.basurveyapp.dto.response.CourseGroupModelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "api-course-group", url = "http://127.0.0.1:8081/coursegroup")
public interface ICourseGroupManager {

    @GetMapping("/findall")
    ResponseEntity<List<CourseGroupModelResponse>> findall();
}
