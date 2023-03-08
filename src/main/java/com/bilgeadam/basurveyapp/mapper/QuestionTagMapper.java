package com.bilgeadam.basurveyapp.mapper;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.QuestionTag;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface QuestionTagMapper {

    QuestionTagMapper QUESTION_TAG_MAPPER = Mappers.getMapper(QuestionTagMapper.class);

    QuestionTag toQuestionTag(CreateTagDto dto);

}
