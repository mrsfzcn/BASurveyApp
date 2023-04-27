package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.TrainerUpdateDto;
import com.bilgeadam.basurveyapp.dto.response.AssistantTrainerResponseDto;
import com.bilgeadam.basurveyapp.dto.response.MasterTrainerResponseDto;
import com.bilgeadam.basurveyapp.dto.response.TrainerResponseDto;
import com.bilgeadam.basurveyapp.entity.Trainer;
import com.bilgeadam.basurveyapp.entity.tags.TrainerTag;
import com.bilgeadam.basurveyapp.exceptions.custom.TrainerNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.TrainerTagNotFoundException;
import com.bilgeadam.basurveyapp.mapper.TrainerMapper;
import com.bilgeadam.basurveyapp.repositories.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainerTagService trainerTagService;

    public Boolean createTrainer(Trainer trainer) {
        trainerRepository.save(trainer);
        return true;
    }
    public TrainerResponseDto updateTrainer (TrainerUpdateDto dto){
        Optional<Trainer> trainer = trainerRepository.findActiveById(dto.getTrainerOid());
        Optional<TrainerTag> trainerTag = trainerTagService.findOptionalTrainerTagById(dto.getTrainerTagOid());

        if (trainerTag.isEmpty()) {
            throw new TrainerTagNotFoundException("Trainer tag is not found");
        }
        if (trainer.isEmpty()) {
            throw new TrainerNotFoundException("Trainer is not found");
        } else {
            trainer.get().getTrainerTags().add(trainerTag.get());
            trainerTag.get().getTargetEntities().add(trainer.get());
            trainerRepository.save(trainer.get());
            trainerTagService.save(trainerTag.get());
        }
        return TrainerMapper.INSTANCE.toTrainerResponseDto(trainer.get());
    }
    public Optional<Trainer> findTrainerByUserOid(Long oid) {

        return trainerRepository.findTrainerByUserOid(oid);
    }

    public Optional<Trainer> findActiveById(Long trainerOid) {

        return trainerRepository.findActiveById(trainerOid);
    }

    public List<MasterTrainerResponseDto> getMasterTrainerList() {

        List<Trainer> masterTrainers = trainerRepository.findAllMasterTrainers();

        return TrainerMapper.INSTANCE.toMasterTrainerResponseDtoList(masterTrainers);
    }


    public List<AssistantTrainerResponseDto> getAssistantTrainerList() {

        List<Trainer> assistantTrainers = trainerRepository.findAllAssistantTrainers();

        return TrainerMapper.INSTANCE.toAssistantTrainerResponseDtos(assistantTrainers);
    }

    public Optional<Trainer> findActiveByEmail(String email) {
        return trainerRepository.findActiveByEmail(email);
    }
}
