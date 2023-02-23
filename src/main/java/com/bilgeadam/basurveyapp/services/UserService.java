package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.UserUpdateRequestDto;
import com.bilgeadam.basurveyapp.dto.response.UserResponseDto;
import com.bilgeadam.basurveyapp.entity.Classroom;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
import com.bilgeadam.basurveyapp.repositories.QuestionRepository;
import com.bilgeadam.basurveyapp.repositories.QuestionTypeRepository;
import com.bilgeadam.basurveyapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final QuestionTypeRepository questionTypeRepository;

    public List<UserResponseDto> getStudentList() {
        List<User> students = userRepository.findStudents();
        return students.stream().map(student -> UserResponseDto.builder()
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .classrooms(student.getClassrooms().stream().map(Classroom::getName).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }

    public List<UserResponseDto> getMasterTrainerList() {
        List<User> students = userRepository.findMasterTrainers();
        return students.stream().map(student -> UserResponseDto.builder()
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .classrooms(student.getClassrooms().stream().map(Classroom::getName).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }

    public List<UserResponseDto> getAssistantTrainerList() {
        List<User> students = userRepository.findAssitantTrainers();
        return students.stream().map(student -> UserResponseDto.builder()
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .classrooms(student.getClassrooms().stream().map(Classroom::getName).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }

    public List<UserResponseDto> getManagerList() {
        List<User> students = userRepository.findManagers();
        return students.stream().map(student -> UserResponseDto.builder()
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .classrooms(student.getClassrooms().stream().map(Classroom::getName).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }

    public List<UserResponseDto> getAdminList() {
        List<User> students = userRepository.findAdmins();
        return students.stream().map(student -> UserResponseDto.builder()
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .classrooms(student.getClassrooms().stream().map(Classroom::getName).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }

    public Page<User> getUserPage(Pageable pageable) {
        // getCurrentUser()
        // user check
        return userRepository.findAllActive(pageable);
    }

    public User updateUser(Long userId, UserUpdateRequestDto dto) {
        // getCurrentUser()
        // user check
        Optional<User> userToBeUpdated = userRepository.findActiveById(userId);
        if (userToBeUpdated.isEmpty()) {
            throw new ResourceNotFoundException("User is not found");
        }
        userToBeUpdated.get().setFirstName(dto.getFirstName());
        userToBeUpdated.get().setLastName(dto.getLastName());
        return userRepository.save(userToBeUpdated.get());
    }

    public void deleteUser(Long userId) {
        // getCurrentUser()
        // user check
        Optional<User> userToBeDeleted = userRepository.findActiveById(userId);
        if (userToBeDeleted.isEmpty()) {
            throw new ResourceNotFoundException("User is not found");
        }
        userRepository.softDeleteById(userToBeDeleted.get().getOid());
    }

    public User findByOid(Long userId) {
        // getCurrentUser()
        // user check
        Optional<User> userById = userRepository.findActiveById(userId);
        if (userById.isEmpty()) {
            throw new ResourceNotFoundException("User is not found");
        }
        return userById.get();
    }

    public List<Question> getQuestionByRole(String role) {
    Optional<List<Question>> questions = questionRepository.findByRole(role);
    if(questions.isEmpty()){
        throw  new ResourceNotFoundException("Questions are not found");
    }
       return questions.get();

    }
}
