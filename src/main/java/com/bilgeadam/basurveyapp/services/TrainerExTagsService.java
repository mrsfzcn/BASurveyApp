package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.TrainerExTags;
import com.bilgeadam.basurveyapp.repositories.TrainerExTagsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerExTagsService {
    private final TrainerExTagsRepository trainerExTagsRepository;

    public void saveExTrainerTag(TrainerExTags trainerExTags){
        trainerExTagsRepository.save(trainerExTags);
    }
}
