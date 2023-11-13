package com.bilgeadam.basurveyapp.manager;

import com.bilgeadam.basurveyapp.dto.response.CourseGroupModelResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "api-course-group", url = "${feign.url}/coursegroup")
public interface ICourseGroupManager {

    @GetMapping("/find-all")
    ResponseEntity<List<CourseGroupModelResponseDto>> findall();
}
