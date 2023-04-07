package com.bilgeadam.basurveyapp.mapper;

import com.bilgeadam.basurveyapp.dto.response.StudentTagDetailResponseDto;
import com.bilgeadam.basurveyapp.dto.response.TrainerTagDetailResponseDto;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.entity.tags.TrainerTag;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    List<StudentTagDetailResponseDto> toStudentTagDetailResponseDtoList(final List<StudentTag> studentTags);
    List<TrainerTagDetailResponseDto> toTrainerTagDetailResponseDtoList(final List<TrainerTag> trainerTags);


}