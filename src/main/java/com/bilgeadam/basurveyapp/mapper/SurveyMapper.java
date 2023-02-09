package com.bilgeadam.basurveyapp.mapper;

import com.bilgeadam.basurveyapp.dto.response.SurveyResponseDto;
import com.bilgeadam.basurveyapp.entity.Survey;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD, uses = {ClassroomMapper.class, QuestionMapper.class, UserMapper.class})
public interface SurveyMapper {

    SurveyMapper SURVEY_MAPPER = Mappers.getMapper(SurveyMapper.class);

    @Mapping(target = "surveyClassroomResponseDtoList", source = "classrooms")
    @Mapping(target = "surveyQuestionResponseDtoList", source = "questions")
    @Mapping(target = "surveyUserResponseDtoList", source = "users")
    @Mapping(target = "surveyOid", source = "oid")
    SurveyResponseDto toSurveyResponseDto(Survey survey);
    List<SurveyResponseDto> toSurveyResponseDtos(List<Survey> surveys);
}