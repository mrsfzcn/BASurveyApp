package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.Manager;
import com.bilgeadam.basurveyapp.repositories.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ManagerService {
    private final ManagerRepository managerRepository;

    public Boolean createManager(Manager manager) {
        managerRepository.save(manager);
        return true;
    }


    public void deleteByManagerOid(Long oid) {
        Optional<Manager> manager = managerRepository.findByUserOid(oid);
        managerRepository.softDeleteById(manager.get().getOid());
    }
}
