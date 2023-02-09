package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.AddUsersToClassroomDto;
import com.bilgeadam.basurveyapp.dto.request.CreateClassroomDto;
import com.bilgeadam.basurveyapp.dto.request.DeleteUserInClassroomDto;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.Classroom;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.entity.enums.Role;
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
        for (User user : userList) {
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

    public ClassroomResponseDto findById(Long classroomId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findActiveById(classroomId);
        if (optionalClassroom.isEmpty()) {
            throw new ClassroomNotFoundException("Classroom is not found");
        }
        Classroom classroom = optionalClassroom.get();
        List<User> users = classroom.getUsers();
        List<Survey> surveys = classroom.getSurveys();
        return ClassroomResponseDto.builder()
                .classroomName(classroom.getName())
                .classroomOid(classroom.getOid())
                .students(users.stream()
                        .filter(student -> student.getRole() == Role.STUDENT)
                        .map(student -> UserSimpleResponseDto.builder()
                                .firstName(student.getFirstName())
                                .lastName(student.getLastName())
                                .email(student.getEmail())
                                .build())
                        .collect(Collectors.toList()))
                .masterTrainers(users.stream()
                        .filter(mt -> mt.getRole() == Role.MASTER_TRAINER)
                        .map(mt -> UserResponseDto.builder()
                                .firstName(mt.getFirstName())
                                .lastName(mt.getLastName())
                                .email(mt.getEmail())
                                .classrooms(mt.getClassrooms().stream().map(Classroom::getName).collect(Collectors.toList()))
                                .build())
                        .collect(Collectors.toList()))
                .assistantTrainers(users.stream()
                        .filter(at -> at.getRole() == Role.ASSISTANT_TRAINER)
                        .map(at -> UserResponseDto.builder()
                                .firstName(at.getFirstName())
                                .lastName(at.getLastName())
                                .email(at.getEmail())
                                .classrooms(at.getClassrooms().stream().map(Classroom::getName).collect(Collectors.toList()))
                                .build())
                        .collect(Collectors.toList()))
                .surveys(surveys.stream()
                        .map(survey -> SurveyResponseDto.builder()
                                .surveyOid(survey.getOid())
                                .surveyTitle(survey.getSurveyTitle())
                                .courseTopic(survey.getCourseTopic())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public List<ClassroomSimpleResponseDto> findAll() {
        List<Classroom> classrooms = classroomRepository.findAllActive();
        if (classrooms.isEmpty()) {
            throw new ClassroomNotFoundException("Classroom is not found");
        }
        return classrooms.stream().map(classroom -> ClassroomSimpleResponseDto.builder()
                .oid(classroom.getOid())
                .name(classroom.getName())
                .build()
        ).collect(Collectors.toList());
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
