package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateClassroomDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateClassroomDto;
import com.bilgeadam.basurveyapp.dto.response.AllClassroomsResponseDto;
import com.bilgeadam.basurveyapp.dto.response.ClassroomFindByIdResponseDto;
import com.bilgeadam.basurveyapp.entity.Classroom;
import com.bilgeadam.basurveyapp.repositories.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassroomService {
    private final ClassroomRepository classroomRepository;

    public void createClassroom(CreateClassroomDto createClassroomDto) {
        //TODO check if exists
        Classroom classroom = Classroom.builder()
                .name(createClassroomDto.getName())
                .users(createClassroomDto.getUsers()).build();
        classroomRepository.save(classroom);
    }

    public Boolean updateClassroom(UpdateClassroomDto updateClassroomDto) {
        Optional<Classroom> updateClassroom = classroomRepository.findActiveById(updateClassroomDto.getClassroomOid());
        if (updateClassroom.isEmpty()) {
            // TODO better exception
            throw new RuntimeException("Classroom is not found");
        } else {
            updateClassroom.get().setUsers(updateClassroomDto.getUsers());
            Classroom classroom = updateClassroom.get();
            classroomRepository.save(classroom);
            return true;
        }
    }

    public ClassroomFindByIdResponseDto findById(Long classroomId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findActiveById(classroomId);
        if (optionalClassroom.isEmpty()) {
            // TODO better exception
            throw new RuntimeException("Classroom is not found");
        }
        return ClassroomFindByIdResponseDto.builder()
                .name(optionalClassroom.get().getName())
                .users(optionalClassroom.get().getUsers())
                .build();
    }

    public List<AllClassroomsResponseDto> findAll() {
        List<Classroom> findAllList = classroomRepository.findAllActive();
        List<AllClassroomsResponseDto> responseDtoList = new ArrayList<>();
        findAllList.forEach(classroom -> {
            responseDtoList.add(AllClassroomsResponseDto.builder()
                    .name(classroom.getName())
                    .users(classroom.getUsers())
                    .build());
        });
        return responseDtoList;
    }

    public Boolean delete(Long classroomId) {
        Optional<Classroom> deleteClassroom = classroomRepository.findActiveById(classroomId);
        if (deleteClassroom.isEmpty()) {
            // TODO better exception
            throw new RuntimeException("Classroom is not found");
        } else {
            Classroom classroom = deleteClassroom.get();
            classroomRepository.softDelete(classroom);
            return true;
        }
    }
}
