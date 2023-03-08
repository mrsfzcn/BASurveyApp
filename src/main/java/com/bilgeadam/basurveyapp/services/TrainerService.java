package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.Trainer;
import com.bilgeadam.basurveyapp.repositories.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerRepository trainerRepository;

    public Boolean createTrainer(Trainer trainer) {
        trainerRepository.save(trainer);
        return true;
    }
}
