package com.bilgeadam.basurveyapp.mapper;

import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.Trainer;
import com.bilgeadam.basurveyapp.entity.tags.TrainerTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface TrainerMapper {
    TrainerMapper INSTANCE = Mappers.getMapper(TrainerMapper.class);

    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    MasterTrainerResponseDto toMasterTrainerResponseDto(Trainer trainer);
    List<MasterTrainerResponseDto> toMasterTrainerResponseDtoList(List<Trainer> trainers);
    List<MasterTrainerTagResponseDto> toMasterTrainerTagResponseDto(Set<TrainerTag> trainerTags);



    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    AssistantTrainerResponseDto toAssistantTrainerResponseDto(Trainer trainer);
    List<AssistantTrainerResponseDto> toAssistantTrainerResponseDtos(List<Trainer> trainers);
    List<AssistantTrainerTagResponseDto> toAssistantTrainerTagResponseDto(Set<TrainerTag> trainerTags);

    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    TrainerResponseDto toTrainerResponseDto (Trainer trainer);
}
