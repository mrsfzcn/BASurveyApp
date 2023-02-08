package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.AddUsersToClassroomDto;
import com.bilgeadam.basurveyapp.dto.request.CreateClassroomDto;
import com.bilgeadam.basurveyapp.dto.request.DeleteUserInClassroomDto;
import com.bilgeadam.basurveyapp.dto.response.ClassroomFindByIdResponseDto;
import com.bilgeadam.basurveyapp.dto.response.ClassroomUsersResponseDto;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;

    @Transactional
    public Boolean createClassroom(CreateClassroomDto createClassroomDto) {
        Optional<Classroom> optionalClassroom = classroomRepository.findActiveByName(createClassroomDto.getName());
        if (optionalClassroom.isPresent()) {
            throw new ClassroomExistException("Classroom already exists");
        }
        Classroom classroom = Classroom.builder()
                .name(createClassroomDto.getName())
                .build();
        classroomRepository.save(classroom);
        return true;
    }

    @Transactional
    public Boolean addUserToClassroom(AddUsersToClassroomDto addUsersToClassroomDto) {
        Optional<Classroom> optionalClassroom = classroomRepository.findActiveByName(addUsersToClassroomDto.getClassroomName());
        List<User> userList = userRepository.findAllByEmails(addUsersToClassroomDto.getUserEmails());
        if (optionalClassroom.isEmpty()) {
            throw new ClassroomNotFoundException("Classroom is not found");
        } else if (userList.isEmpty()) {
            throw new UserDoesNotExistsException("User is not found");
        }
        Classroom classroom = optionalClassroom.get();
        for (User user:userList) {
            classroom.getUsers().add(user);
        }
        classroomRepository.save(classroom);
        return true;
    }

    @Transactional
    public void deleteUserFromClassroom(DeleteUserInClassroomDto deleteUserInClassroomDto) {
        Optional<Classroom> optionalClassroom = classroomRepository.findActiveById(deleteUserInClassroomDto.getClassroomOid());
        Optional<User> optionalUser = userRepository.findByEmail(deleteUserInClassroomDto.getUserEmail());
        if (optionalClassroom.isEmpty()) {
            throw new ClassroomNotFoundException("Classroom is not found");
        } else if (optionalUser.isEmpty()) {
            throw new UserDoesNotExistsException("User is not found");
        }
        Classroom classroom = optionalClassroom.get();
        User user = optionalUser.get();
        classroom.getUsers().remove(user);
        classroomRepository.save(classroom);
    }

    @Transactional
    public ClassroomFindByIdResponseDto findById(Long classroomId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findActiveById(classroomId);
        if (optionalClassroom.isEmpty()) {
            throw new ClassroomNotFoundException("Classroom is not found");
        }
        Classroom classroom = optionalClassroom.get();
        List<ClassroomUsersResponseDto> users = new ArrayList<>();
        for (User user : classroom.getUsers()) {
            ClassroomUsersResponseDto classroomUsersResponseDto = ClassroomUsersResponseDto.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .role(String.valueOf(user.getRole()))
                    .build();
            users.add(classroomUsersResponseDto);
        }
        return ClassroomFindByIdResponseDto.builder()
                .name(classroom.getName())
                .users(users)
                .build();
    }

    @Transactional
    public List<String> findAll() {
        Optional<List<Classroom>> optionalClassrooms = Optional.ofNullable(classroomRepository.findAllActive());
        if (optionalClassrooms.isEmpty()) {
            throw new ClassroomNotFoundException("Classroom is not found");
        }
//        List<AllClassroomsResponseDto> allClassroomsResponseDtoList = new ArrayList<>();
//        for (Classroom classroom : optionalClassrooms.get()) {
//            AllClassroomsResponseDto allClassroomsResponseDto = AllClassroomsResponseDto.builder()
//                    .oid(classroom.getOid())
//                    .name(classroom.getName())
//                    .build();
//            allClassroomsResponseDtoList.add(allClassroomsResponseDto);
//        }
        return optionalClassrooms.get().stream().map(Classroom::getName).collect(Collectors.toList());
    }

    @Transactional
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
