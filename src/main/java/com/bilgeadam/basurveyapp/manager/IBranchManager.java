package com.bilgeadam.basurveyapp.manager;

import com.bilgeadam.basurveyapp.dto.response.BranchModelResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(url = "${feign.url}/branch",name = "api-branch")
public interface IBranchManager {

    @GetMapping("/find-all")
    ResponseEntity<List<BranchModelResponseDto>> findAll();

    @GetMapping("/find-by-id/{id}")
    ResponseEntity<BranchModelResponseDto> findById(@PathVariable Long id);
}
