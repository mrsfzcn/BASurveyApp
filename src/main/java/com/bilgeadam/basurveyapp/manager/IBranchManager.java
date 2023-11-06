package com.bilgeadam.basurveyapp.manager;

import com.bilgeadam.basurveyapp.dto.response.BranchModelResponse;
import com.bilgeadam.basurveyapp.entity.Branch;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(url = "http://localhost:8081/branch",name = "api-branch")
public interface IBranchManager {

    @GetMapping("/findAll")
    ResponseEntity<List<BranchModelResponse>> findAll();
}
