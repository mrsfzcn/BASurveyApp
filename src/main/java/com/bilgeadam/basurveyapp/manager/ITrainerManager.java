package com.bilgeadam.basurveyapp.manager;

import com.bilgeadam.basurveyapp.dto.response.TrainerModelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "api-trainer", url = "http://localhost:8081/trainer")
public interface ITrainerManager {
    @GetMapping("/findall")
    ResponseEntity<List<TrainerModelResponse>> findAll();
}
