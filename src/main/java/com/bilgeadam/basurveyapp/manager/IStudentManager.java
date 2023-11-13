package com.bilgeadam.basurveyapp.manager;

import com.bilgeadam.basurveyapp.dto.request.StudentModelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "api-student", url = "${feign.url}/student")
public interface IStudentManager {
    @GetMapping("/find-all")
    ResponseEntity<List<StudentModelResponse>> findAll();
}
