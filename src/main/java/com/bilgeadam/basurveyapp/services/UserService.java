package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.UserUpdateRequestDto;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
import com.bilgeadam.basurveyapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getUserList() {
        // getCurrentUser()
        // user check
        return userRepository.findAllActive();
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
        userRepository.softDelete(userToBeDeleted.get());
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
}
