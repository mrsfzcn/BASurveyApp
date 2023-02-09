package com.bilgeadam.basurveyapp.mapper;

import com.bilgeadam.basurveyapp.dto.response.SurveyQuestionResponseDto;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.QuestionType;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface QuestionMapper {

    QuestionMapper QUESTION_MAPPER = Mappers.getMapper(QuestionMapper.class);

    @Mapping(target = "questionType", source = "questionType", qualifiedByName = "map")
    SurveyQuestionResponseDto toSurveyQuestionResponseDto(Question question);
    List<SurveyQuestionResponseDto> toSurveyQuestionResponseDtos(List<Question> questions);

    @Named("map")
    default String map(QuestionType questionType){
        return questionType.toString();
    }
}