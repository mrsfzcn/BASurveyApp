package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.Manager;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
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
        if(manager.isEmpty())
            throw new ResourceNotFoundException("Entity not found");
        manager.get().getUser().setState(State.DELETED);
        managerRepository.save(manager.get());
        managerRepository.softDeleteById(manager.get().getOid());
    }
}
