package com.bilgeadam.basurveyapp.mapper;

import com.bilgeadam.basurveyapp.dto.response.SurveyResponseDto;
import com.bilgeadam.basurveyapp.entity.Survey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ClassroomMapper.class, QuestionMapper.class, UserMapper.class})
public interface SurveyMapper {

    SurveyMapper SURVEY_MAPPER = Mappers.getMapper(SurveyMapper.class);


    @Mapping(target = "classroomResponseDtos", source = "classrooms")
    @Mapping(target = "questionResponseDtos", source = "questions")
    @Mapping(target = "userResponseDtos", source = "users")
    SurveyResponseDto toSurveyResponseDto(Survey survey);
    List<SurveyResponseDto> toSurveyResponseDtos(List<Survey> surveys);
}