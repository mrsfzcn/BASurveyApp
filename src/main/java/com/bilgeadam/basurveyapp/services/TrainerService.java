package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.StudentUpdateDto;
import com.bilgeadam.basurveyapp.dto.request.TrainerUpdateDto;
import com.bilgeadam.basurveyapp.dto.response.AssistantTrainerResponseDto;
import com.bilgeadam.basurveyapp.dto.response.MasterTrainerResponseDto;
import com.bilgeadam.basurveyapp.dto.response.StudentResponseDto;
import com.bilgeadam.basurveyapp.dto.response.TrainerResponseDto;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.Trainer;
import com.bilgeadam.basurveyapp.entity.tags.TrainerTag;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
import com.bilgeadam.basurveyapp.mapper.StudentMapper;
import com.bilgeadam.basurveyapp.mapper.TrainerMapper;
import com.bilgeadam.basurveyapp.repositories.TrainerRepository;
import com.bilgeadam.basurveyapp.repositories.TrainerTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainerTagService trainerTagService;
    private final TrainerTagRepository trainerTagRepository;
    public Boolean createTrainer(Trainer trainer) {
        trainerRepository.save(trainer);
        return true;
    }
    public TrainerResponseDto updateTrainer (TrainerUpdateDto dto){
        Optional<Trainer> trainer = trainerRepository.findActiveById(dto.getTrainerOid());
        Optional<TrainerTag> trainerTag = trainerTagRepository.findActiveById(dto.getTrainerTagOid());

        if (trainerTag.isEmpty()) {
            throw new ResourceNotFoundException("Trainer tag is not found");
        }
        if (trainer.isEmpty()) {
            throw new ResourceNotFoundException("Trainer is not found");
        } else {
            trainer.get().getTrainerTags().add(trainerTag.get());
            trainerTag.get().getTargetEntities().add(trainer.get());
            trainerRepository.save(trainer.get());
            trainerTagRepository.save(trainerTag.get());
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

        List<MasterTrainerResponseDto> dto = TrainerMapper.INSTANCE.toMasterTrainerResponseDtoList(masterTrainers);
        return dto;
    }


    public List<AssistantTrainerResponseDto> getAssistantTrainerList() {

        List<Trainer> assistantTrainers = trainerRepository.findAllAssistantTrainers();

        List<AssistantTrainerResponseDto> dto = TrainerMapper.INSTANCE.toAssistantTrainerResponseDtos(assistantTrainers);
        return dto;
    }
}
