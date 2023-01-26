package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateClassroomDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateClassroomDto;
import com.bilgeadam.basurveyapp.dto.response.AllClassroomsResponseDto;
import com.bilgeadam.basurveyapp.dto.response.ClassroomFindByIdResponseDto;
import com.bilgeadam.basurveyapp.entity.Classroom;
import com.bilgeadam.basurveyapp.repositories.IClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassroomService {
    private final IClassroomRepository classroomRepository;

    public void createClassroom(CreateClassroomDto createClassroomDto, Long userOid) {
        Classroom classroom = Classroom.builder()
                .name(createClassroomDto.getName())
                .users(createClassroomDto.getUsers()).build();
        classroomRepository.save(classroom);
    }

    public Boolean updateClassroom(UpdateClassroomDto updateClassroomDto, Long userOid) {
        Optional<Classroom> updateClassroom = classroomRepository.findById(updateClassroomDto.getClassroomOid());
        if (updateClassroom.isEmpty()) {
            return false;
        } else {
            updateClassroom.get().setUsers(updateClassroomDto.getUsers());
            Classroom classroom = updateClassroom.get();
            classroomRepository.save(classroom);
            return true;
        }
    }

    public ClassroomFindByIdResponseDto findById(Long classroomId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isEmpty()) {
            return null;
        } else {
            ClassroomFindByIdResponseDto dto = ClassroomFindByIdResponseDto.builder()
                    .name(optionalClassroom.get().getName())
                    .users(optionalClassroom.get().getUsers())
                    .build();

            return dto;
        }

    }

    public List<AllClassroomsResponseDto> findAll() {
        List<Classroom> findAllList = classroomRepository.findAll();
        List<AllClassroomsResponseDto> responseDtoList = new ArrayList<>();
        findAllList.forEach(classroom -> {
            responseDtoList.add(AllClassroomsResponseDto.builder()
                    .name(classroom.getName())
                    .users(classroom.getUsers())
                    .build());
        });
        return responseDtoList;
    }

    public Boolean delete(Long classroomId, Long userOid) {

        Optional<Classroom> deleteClassroom = classroomRepository.findById(classroomId);
        if (deleteClassroom.isEmpty()) {
            return false;
        } else {
            Classroom classroom = deleteClassroom.get();
            classroomRepository.softDelete(classroom);
            return true;
        }
    }
}
