package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.Manager;
import com.bilgeadam.basurveyapp.repositories.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ManagerService {
    private final ManagerRepository managerRepository;

    public Boolean createManager(Manager manager) {
        managerRepository.save(manager);
        return true;
    }


}
