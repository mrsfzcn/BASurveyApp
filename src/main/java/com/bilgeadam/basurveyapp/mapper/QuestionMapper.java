package com.bilgeadam.basurveyapp.mapper;

import com.bilgeadam.basurveyapp.dto.request.CreateQuestionDto;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.QuestionType;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.services.QuestionService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuestionMapper {

    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

//    @Mapping(target = "questionType", source = "questionType", qualifiedByName = "map")
//    SurveyQuestionResponseDto toSurveyQuestionResponseDto(Question question);
//    List<SurveyQuestionResponseDto> toSurveyQuestionResponseDtos(List<Question> questions);
//
//    @Named("map")
//    default String map(QuestionType questionType){
//        return questionType.toString();
//    }

    /**
     * questionCreate
     */

    Question toQuestion(final CreateQuestionDto createQuestionDto);


    /**
     *
     * findAll
     */
    @Mapping(target = "questionTypeOid", source = "questionType.oid")
    @Mapping(target = "questionOid", source = "oid")
    QuestionResponseDto toQuestionResponseDto(Question question);
    List<QuestionResponseDto> toQuestionResponseDtos(List<Question> questions);

    List<QuestionTagResponseDto> toQuestionTagResponseDto(List<QuestionTag> questionTags);

    /**
     * findById
     */

    @Mapping(target = "questionType", source = "questionType.questionType")
    QuestionFindByIdResponseDto toQuestionFindByIdResponseDto(Question question);

    @Mapping(target = "surveyOid", source = "oid")
    SurveySimpleResponseDto toSurveySimpleResponseDto(Survey survey);





}