package com.bilgeadam.basurveyapp.manager;

import com.bilgeadam.basurveyapp.dto.response.TrainerModelResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "api-trainer", url = "${feign.url}/trainer")
public interface ITrainerManager {
    @GetMapping("/find-all")
    ResponseEntity<List<TrainerModelResponseDto>> findAll();
}
