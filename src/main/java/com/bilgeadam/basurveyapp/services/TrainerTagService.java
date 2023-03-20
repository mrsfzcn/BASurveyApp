package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.entity.Trainer;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.entity.tags.TrainerTag;
import com.bilgeadam.basurveyapp.repositories.TrainerTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerTagService {
    private final TrainerTagRepository trainerTagRepository;
    public void createTag(CreateTagDto dto) {
        TrainerTag trainerTag = TrainerTag.builder()
                .tagString(dto.getTagString())
                .build();
        trainerTagRepository.save(trainerTag);
    }
    public Set<Long> getTrainerTagsOids(Trainer trainer) {
        return trainerTagRepository.findActiveTrainerTagsByTrainerId(trainer.getOid()).stream().map(TrainerTag::getOid).collect(Collectors.toSet());
    }

    public Optional<TrainerTag> findActiveById(Long trainerTagOid) {
        return trainerTagRepository.findActiveById(trainerTagOid);
    }

    public Set<TrainerTag> getTrainerTags(Trainer trainer) {
        return trainerTagRepository.findActiveTrainerTagsByTrainerId(trainer.getOid());
    }
    public Boolean delete(Long trainerTagOid) {
        Optional<TrainerTag> deleteTag = trainerTagRepository.findActiveById(trainerTagOid);
        if (deleteTag.isEmpty()) {
            throw new RuntimeException("Tag is not found");
        } else {
            trainerTagRepository.softDeleteById(deleteTag.get().getOid(),"trainertags");
            return true;
        }
    }
}
