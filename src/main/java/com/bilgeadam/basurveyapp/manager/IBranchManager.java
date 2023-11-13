package com.bilgeadam.basurveyapp.manager;

import com.bilgeadam.basurveyapp.dto.response.BranchModelResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(url = "${feign.url}/branch",name = "api-branch")
public interface IBranchManager {

    @GetMapping("/find-all")
    ResponseEntity<List<BranchModelResponseDto>> findAll();
}
