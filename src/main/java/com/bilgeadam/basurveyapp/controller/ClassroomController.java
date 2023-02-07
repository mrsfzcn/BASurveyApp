package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.AddUsersToClassroomDto;
import com.bilgeadam.basurveyapp.dto.request.DeleteUserInClassroomDto;
import com.bilgeadam.basurveyapp.dto.request.CreateClassroomDto;
import com.bilgeadam.basurveyapp.dto.request.FindByIdRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AllClassroomsResponseDto;
import com.bilgeadam.basurveyapp.dto.response.ClassroomFindByIdResponseDto;
import com.bilgeadam.basurveyapp.services.ClassroomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
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

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'MASTER_TRAINER', 'ASISTANT_TRAINER', 'STUDENT')")
    @PostMapping("/create")
    public ResponseEntity<Void> createClassroom(@RequestBody @Valid CreateClassroomDto createClassroomDto) {
        classroomService.createClassroom(createClassroomDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'MASTER_TRAINER', 'ASISTANT_TRAINER', 'STUDENT')")
    @PutMapping("/addUserToClassroom")
    public ResponseEntity<Boolean> addUsers(@RequestBody @Valid AddUsersToClassroomDto addUsersToClassroomDto) {
        classroomService.addUserToClassroom(addUsersToClassroomDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'MASTER_TRAINER', 'ASISTANT_TRAINER', 'STUDENT')")
    @DeleteMapping("/deleteUserFromClassroom")
    public ResponseEntity<Boolean> deleteUsers(@RequestBody @Valid DeleteUserInClassroomDto deleteUserInClassroomDto) {
        classroomService.deleteUserFromClassroom(deleteUserInClassroomDto);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'MASTER_TRAINER', 'ASISTANT_TRAINER', 'STUDENT')")
    @GetMapping("/findbyid/{oid}")
    public ResponseEntity<ClassroomFindByIdResponseDto> findById(@RequestParam Long oid) {
        return ResponseEntity.ok(classroomService.findById(oid));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'MASTER_TRAINER', 'ASISTANT_TRAINER', 'STUDENT')")
    @GetMapping("/findall")
    public ResponseEntity<List<AllClassroomsResponseDto>> findAll() {
        List<AllClassroomsResponseDto> responseDtoList = classroomService.findAll();
        return ResponseEntity.ok(responseDtoList);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'MASTER_TRAINER', 'ASISTANT_TRAINER', 'STUDENT')")
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody @Valid Long classroomId) {
        return ResponseEntity.ok(classroomService.delete(classroomId));
    }

}


