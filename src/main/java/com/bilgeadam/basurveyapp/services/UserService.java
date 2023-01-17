package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.repositories.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepositoryImpl userRepository;

    public List<User> getUserList() {
        return userRepository.findAll();
    }

    public Page<User> getUserPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
