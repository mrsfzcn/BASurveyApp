package com.bilgeadam.basurveyapp.mapper;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionTagResponseDto;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuestionTagMapper {

    QuestionTagMapper INSTANCE = Mappers.getMapper(QuestionTagMapper.class);

    QuestionTag toQuestionTag(CreateTagDto dto);

    List<QuestionTagResponseDto> toQuestionTagResponseDtoList(Set<QuestionTag> questionTags);

}
