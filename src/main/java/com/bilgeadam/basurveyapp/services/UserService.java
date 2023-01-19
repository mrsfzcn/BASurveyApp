package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.UserCreateRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UserUpdateRequestDto;
import com.bilgeadam.basurveyapp.entity.Classroom;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.entity.enums.Role;
import com.bilgeadam.basurveyapp.repositories.UserRepositoryImpl;
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

    private final UserRepositoryImpl userRepository;

    public List<User> getUserList() {
        // getCurrentUser()
        // user check
        return userRepository.findAll().stream()
            .filter(User::isActive)
            .collect(Collectors.toList());
    }

    public Page<User> getUserPage(Pageable pageable) {
        // getCurrentUser()
        // user check
        return userRepository.findAll(pageable);
    }

    public User createUser(UserCreateRequestDto dto) {
        // getCurrentUser()
        // user check
        User user = User.builder()
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .email(dto.getEmail())
            //.auth()
            .role(Role.valueOf(dto.getRole()))
            .classrooms(List.of(Classroom.builder().name(dto.getClassroomName()).build()))
            .build();
        return userRepository.save(user, 0L);
    }
    public User updateUser(Long userId, UserUpdateRequestDto dto) {
        // getCurrentUser()
        // user check
        Optional<User> userToBeUpdated = userRepository.findByOid(userId);
        if(userToBeUpdated.isEmpty()){
            // user not found exception is needed.
        }
        userToBeUpdated.get().setFirstName(dto.getFirstName());
        userToBeUpdated.get().setLastName(dto.getLastName());
        return userRepository.update(userToBeUpdated.get(), 0L);
    }
    public void deleteUser(Long userId) {
        // getCurrentUser()
        // user check
        Optional<User> userToBeDeleted = userRepository.findByOid(userId);
        if(userToBeDeleted.isEmpty()){
            // user not found exception is needed.
        }
        userRepository.delete(userToBeDeleted.get(), 0L);
    }

    public User findByOid(Long userId) {
        // getCurrentUser()
        // user check
        Optional<User> userById = userRepository.findByOid(userId);
        if(userById.isEmpty()){
            // user not found exception is needed.
        }
        return userById.get();
    }
}
