package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.AddUserToClassroomDto;
import com.bilgeadam.basurveyapp.dto.request.CreateClassroomDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateClassroomDto;
import com.bilgeadam.basurveyapp.dto.response.AllClassroomsResponseDto;
import com.bilgeadam.basurveyapp.dto.response.ClassroomFindByIdResponseDto;
import com.bilgeadam.basurveyapp.entity.Classroom;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.exceptions.custom.ClassroomExistException;
import com.bilgeadam.basurveyapp.exceptions.custom.ClassroomNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.UserDoesNotExistsException;
import com.bilgeadam.basurveyapp.repositories.ClassroomRepository;
import com.bilgeadam.basurveyapp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;

    public void createClassroom(CreateClassroomDto createClassroomDto) {
        Optional<Classroom> optionalClassroom = classroomRepository.findActiveByName(createClassroomDto.getName());
        if (optionalClassroom.isPresent()) {
            throw new ClassroomExistException("Classroom already exists");
        }
        Classroom classroom = Classroom.builder()
                .name(createClassroomDto.getName())
                .build();
        classroomRepository.save(classroom);
    }

    @Transactional
    //todo email listesi üzerinden user'a ulaşma
    public void addUserToClassroom(AddUserToClassroomDto addUserToClassroomDto) {
        Optional<Classroom> optionalClassroom = classroomRepository.findActiveById(addUserToClassroomDto.getClassroomOid());
        Optional<User> optionalUser = userRepository.findActiveById(addUserToClassroomDto.getUserOid());
        if (optionalClassroom.isEmpty()) {
            throw new ClassroomNotFoundException("Classroom is not found");
        } else if (optionalUser.isEmpty()) {
            throw new UserDoesNotExistsException("User is not found");
        }
        Classroom classroom = optionalClassroom.get();
        User user = optionalUser.get();
        classroom.getUsers().add(user);
        classroomRepository.save(classroom);
    }

    @Transactional
    public void deleteUserFromClassroom(AddUserToClassroomDto addUserToClassroomDto) {
        Optional<Classroom> optionalClassroom = classroomRepository.findActiveById(addUserToClassroomDto.getClassroomOid());
        if (optionalClassroom.isEmpty()) {
            throw new ClassroomNotFoundException("Classroom is not found");
        }
        Classroom classroom = optionalClassroom.get();
        classroom.getUsers().remove(addUserToClassroomDto.getUserOid());
    }

    //todo dtolara bakılacak
    public ClassroomFindByIdResponseDto findById(Long classroomId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findActiveById(classroomId);
        if (optionalClassroom.isEmpty()) {
            throw new ClassroomNotFoundException("Classroom is not found");
        }
        return ClassroomFindByIdResponseDto.builder()
                .name(optionalClassroom.get().getName())
                .users(optionalClassroom.get().getUsers())
                .build();
    }

    public List<AllClassroomsResponseDto> findAll() {
        List<Classroom> findAllList = classroomRepository.findAllActive();
        List<AllClassroomsResponseDto> responseDtoList = new ArrayList<>();
        findAllList.forEach(classroom -> responseDtoList.add(AllClassroomsResponseDto.builder()
                .name(classroom.getName())
                .users(classroom.getUsers())
                .build()));
        return responseDtoList;
    }

    public Boolean delete(Long classroomId) {
        Optional<Classroom> deleteClassroom = classroomRepository.findActiveById(classroomId);
        if (deleteClassroom.isEmpty()) {
            throw new ClassroomNotFoundException("Classroom is not found");
        } else {
            Classroom classroom = deleteClassroom.get();
            classroomRepository.softDelete(classroom);
            return true;
        }
    }
}
