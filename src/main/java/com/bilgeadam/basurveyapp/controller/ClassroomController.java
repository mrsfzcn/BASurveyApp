package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.AddUsersToClassroomDto;
import com.bilgeadam.basurveyapp.dto.request.CreateClassroomDto;
import com.bilgeadam.basurveyapp.dto.request.DeleteUserInClassroomDto;
import com.bilgeadam.basurveyapp.dto.response.ClassroomFindByIdResponseDto;
import com.bilgeadam.basurveyapp.dto.response.ClassroomResponseDto;
import com.bilgeadam.basurveyapp.dto.response.ClassroomSimpleResponseDto;
import com.bilgeadam.basurveyapp.dto.response.SurveyResponseDto;
import com.bilgeadam.basurveyapp.services.ClassroomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<Boolean> createClassroom(@RequestBody @Valid CreateClassroomDto createClassroomDto) {
        return ResponseEntity.ok(classroomService.createClassroom(createClassroomDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/addUsersToClassroom")
    public ResponseEntity<Boolean> addUsers(@RequestBody @Valid AddUsersToClassroomDto addUsersToClassroomDto) {
        return ResponseEntity.ok(classroomService.addUserToClassroom(addUsersToClassroomDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/deleteUserFromClassroom")
    public ResponseEntity<Boolean> deleteUsers(@RequestBody @Valid DeleteUserInClassroomDto deleteUserInClassroomDto) {
        classroomService.deleteUserFromClassroom(deleteUserInClassroomDto);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findbyid/{oid}")
    public ResponseEntity<ClassroomResponseDto> findById(@PathVariable Long oid) {
        return ResponseEntity.ok(classroomService.findById(oid));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/findall")
    public ResponseEntity<List<ClassroomSimpleResponseDto>> findAll() {
        return ResponseEntity.ok(classroomService.findAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody @Valid Long classroomId) {
        return ResponseEntity.ok(classroomService.delete(classroomId));
    }
}


