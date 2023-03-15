package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.response.AssistantTrainerResponseDto;
import com.bilgeadam.basurveyapp.dto.response.MasterTrainerResponseDto;
import com.bilgeadam.basurveyapp.dto.response.StudentResponseDto;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.Trainer;
import com.bilgeadam.basurveyapp.mapper.StudentMapper;
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

    public Boolean createTrainer(Trainer trainer) {
        trainerRepository.save(trainer);
        return true;
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
