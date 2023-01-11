package com.bilgeadam.basurveyapp.service;

import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.repository.IUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getUserList() {
        return userRepository.findAll();
    }
    public Page<User> getUserPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
