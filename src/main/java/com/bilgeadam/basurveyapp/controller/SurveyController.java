package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.SurveyCreateRequestDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyUpdateRequestDto;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.services.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    @GetMapping("/test")
    public String test() {
        return "survey";
    }

    @GetMapping("/list")
    ResponseEntity<List<Survey>> getSurveyList() {
        return ResponseEntity.ok(surveyService.getSurveyList());
    }
    @GetMapping("/page")
    ResponseEntity<Page<Survey>> getSurveyPage(Pageable pageable) {
        return ResponseEntity.ok(surveyService.getSurveyPage(pageable));
    }
    @GetMapping("/{surveyId}")
    ResponseEntity<Survey> findById(@PathVariable("surveyId") Long surveyId){
        return ResponseEntity.ok(surveyService.findByOid(surveyId));
    }
    @PostMapping("/create")
    ResponseEntity<Survey> create(@RequestBody SurveyCreateRequestDto dto) {
        return ResponseEntity.ok(surveyService.create(dto));
    }
    @PostMapping("/update/{surveyId}")
    ResponseEntity<Survey> update(@PathVariable("surveyId") Long surveyId, @RequestBody SurveyUpdateRequestDto dto){
        return ResponseEntity.ok(surveyService.update(surveyId, dto));
    }
    @PutMapping("/delete/{surveyId}")
    ResponseEntity<Void> delete(@PathVariable("surveyId") Long surveyId){
        try{
            surveyService.delete(surveyId);
            return ResponseEntity.ok().build();
        }catch (Exception ex){
            return ResponseEntity.badRequest().build();
        }
    }
}

