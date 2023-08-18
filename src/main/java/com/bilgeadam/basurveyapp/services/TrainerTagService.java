package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.response.FindActiveTrainerTagByIdResponseDto;
import com.bilgeadam.basurveyapp.dto.response.GetTrainerTagsByEmailResponse;
import com.bilgeadam.basurveyapp.dto.response.TrainerTagDetailResponseDto;
import com.bilgeadam.basurveyapp.entity.Trainer;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.entity.tags.TrainerTag;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTagNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.TrainerNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.TrainerTagExistException;
import com.bilgeadam.basurveyapp.exceptions.custom.TrainerTagNotFoundException;
import com.bilgeadam.basurveyapp.mapper.TagMapper;
import com.bilgeadam.basurveyapp.repositories.TrainerTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class TrainerTagService {
    private final TrainerTagRepository trainerTagRepository;
    private final TrainerService trainerService;

    public TrainerTagService(TrainerTagRepository trainerTagRepository,@Lazy TrainerService trainerService) {
        this.trainerTagRepository = trainerTagRepository;
        this.trainerService = trainerService;
    }

    public void createTag(CreateTagDto dto) {
        if(trainerTagRepository.findByTrainerTagName(dto.getTagString()).isPresent()){
            throw new TrainerTagExistException("Trainer Tag already exist!");
        }
        TrainerTag trainerTag = TrainerTag.builder()
                .tagString(dto.getTagString())
                .mainTagOid(dto.getMainTagOid())

                .build();
        trainerTagRepository.save(trainerTag);
    }
    public Set<Long> getTrainerTagsOids(Trainer trainer) {
        return trainerTagRepository.findActiveTrainerTagsByTrainerId(trainer.getOid()).stream().map(TrainerTag::getOid).collect(Collectors.toSet());
    }

    public FindActiveTrainerTagByIdResponseDto findActiveById(Long trainerTagOid) {
        Optional<TrainerTag> trainerTag= trainerTagRepository.findActiveById(trainerTagOid);
        if(trainerTag.isEmpty()){
            throw new TrainerTagNotFoundException("Trainer tag not found");
        }
        return FindActiveTrainerTagByIdResponseDto.builder()
                .oid(trainerTag.get().getOid())
                .tagString(trainerTag.get().getTagString())
        .build();
    }

    public Set<TrainerTag> getTrainerTags(Trainer trainer) {
        return trainerTagRepository.findActiveTrainerTagsByTrainerId(trainer.getOid());
    }
    public Boolean delete(Long trainerTagOid) {
        Optional<TrainerTag> deleteTag = trainerTagRepository.findActiveById(trainerTagOid);
        if (deleteTag.isEmpty()) {
            throw new TrainerTagNotFoundException("Trainer tag not found");
        } else {
            return trainerTagRepository.softDeleteById(deleteTag.get().getOid());
        }
    }

    public boolean deleteByTagString(String tagString) {
        TrainerTag trainerTag = trainerTagRepository.findOptionalByTagString(tagString)
                .orElseThrow(() -> new QuestionTagNotFoundException("Question tag not found"));
        return trainerTagRepository.softDeleteById(trainerTag.getOid());
    }

    public List<TrainerTagDetailResponseDto> getTrainerTagList() {
        return TagMapper.INSTANCE.toTrainerTagDetailResponseDtoList(trainerTagRepository.findAllActive());
    }

    public GetTrainerTagsByEmailResponse getTrainerTagsByEmail(String email) {
        Optional<Trainer> trainer= trainerService.findActiveByEmail(email);

        if(trainer.isEmpty()){
            throw new TrainerNotFoundException("trainer not found");
        }
        List<String> tags =trainerTagRepository.findActiveTrainerTagsByTrainerEmail(trainer.get().getOid());
        if(tags.size()==0){
            throw new TrainerTagNotFoundException("trainer has not have tag");
        }
        return GetTrainerTagsByEmailResponse.builder().trainerTags(tags).build();
    }

    public List<String> findTrainersByTrainerTagString(String tagString) {
        return trainerTagRepository.findByTagString(tagString);
    }

    public void save(TrainerTag trainerTag) {
        trainerTagRepository.save(trainerTag);
    }

    public Optional<TrainerTag> findOptionalTrainerTagById(Long trainerTagOid) {
        Optional<TrainerTag> trainerTag= trainerTagRepository.findActiveById(trainerTagOid);
        if(trainerTag.isEmpty()){
            throw new TrainerTagNotFoundException("Trainer tag not found");
        }
        return trainerTag;
    }
    public List<Long> findTrainerOidByTrainerTagOid(Long oid){
        return trainerTagRepository.findTrainerOidByTrainerTagOid(oid);
    }

    public void updateTagByTagString(String tagString, String newTagString) {
        TrainerTag trainerTag = trainerTagRepository.findOptionalByTagString(tagString)
                .orElseThrow(() -> new QuestionTagNotFoundException("Question Tag not found"));
        trainerTag.setTagString(newTagString);
        trainerTagRepository.save(trainerTag);
    }

    public boolean activeByTagString(String tagString) {
        TrainerTag trainerTag = trainerTagRepository.findOptionalByTagString(tagString)
                .orElseThrow(() -> new QuestionTagNotFoundException("Question tag not found"));
        return trainerTagRepository.activeById(trainerTag.getOid());
    }


}
