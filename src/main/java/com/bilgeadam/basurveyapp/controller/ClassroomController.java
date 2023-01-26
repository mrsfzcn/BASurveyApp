package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.AllClassroomsResponseDto;
import com.bilgeadam.basurveyapp.dto.response.ClassroomFindByIdResponseDto;
import com.bilgeadam.basurveyapp.services.ClassroomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classroom")
@RequiredArgsConstructor
public class ClassroomController {
    private final ClassroomService classroomService;

    @GetMapping("/test")
    public String test() {
        return "classroom";
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createClassroom(@RequestBody @Valid CreateClassroomDto createClassroomDto, @Valid Long userOid) {
        classroomService.createClassroom(createClassroomDto, userOid);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<Boolean> updateClassroom(@RequestBody @Valid UpdateClassroomDto updateClassroomDto, @Valid Long userOid) {
        classroomService.updateClassroom(updateClassroomDto, userOid);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/findbyid")
    public ResponseEntity<ClassroomFindByIdResponseDto> findById(@RequestBody @Valid FindByIdRequestDto findByIdRequestDto) {
        return ResponseEntity.ok(classroomService.findById(findByIdRequestDto.getOid()));
    }

    @GetMapping("/findall")
    public ResponseEntity<List<AllClassroomsResponseDto>> findAll() {
        List<AllClassroomsResponseDto> responseDtoList = classroomService.findAll();
        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody @Valid Long classroomId, @Valid Long userOid) {
        return ResponseEntity.ok(classroomService.delete(classroomId, userOid));
    }

}


