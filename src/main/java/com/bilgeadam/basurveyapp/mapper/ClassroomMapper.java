package com.bilgeadam.basurveyapp.mapper;

import com.bilgeadam.basurveyapp.dto.response.SurveyClassroomResponseDto;
import com.bilgeadam.basurveyapp.entity.Classroom;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface ClassroomMapper {

    ClassroomMapper CLASSROOM_MAPPER = Mappers.getMapper(ClassroomMapper.class);

    SurveyClassroomResponseDto toSurveyClassroomResponseDto(Classroom classroom);
    List<SurveyClassroomResponseDto> toSurveyClassroomResponseDtos(List<Classroom> classrooms);
}