package com.bilgeadam.basurveyapp.mapper;

import com.bilgeadam.basurveyapp.dto.request.CreateQuestionDto;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.Response;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

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
    @Mapping(target = "questionTags", source = "questionTag")
    QuestionResponseDto toQuestionResponseDto(Question question);

    List<QuestionResponseDto> toQuestionResponseDtos(List<Question> questions);

    @Mapping(target = "questionTypeOid", source = "questionType.oid")
    @Mapping(target = "questionOid", source = "oid")
    @Mapping(target = "questionTags", source = "questionTag")
    QuestionsTrainerTypeResponseDto toQuestionsTrainerTypeResponseDto(Question questions);

    List<QuestionsTrainerTypeResponseDto> toQuestionsTrainerTypeResponseDto(Set<Question> questions);

    @Mapping(target = "responseOid", source = "oid")
    @Mapping(target = "questionOid", source = "response.question.oid")
    @Mapping(target = "userOid", source = "response.user.oid")
    @Mapping(target = "surveyOid", source = "response.survey.oid")
    ResponseDto toResponseDto(final Response response);

    List<ResponseDto> toResponseDto(final List<Response> responses);

    List<QuestionTagResponseDto> toQuestionTagResponseDto(List<QuestionTag> questionTags);

    /**
     * findById
     */

    @Mapping(target = "questionType", source = "questionType.questionType")
    @Mapping(target = "questionOid", source = "oid")
    QuestionFindByIdResponseDto toQuestionFindByIdResponseDto(Question question);

    @Mapping(target = "surveyOid", source = "oid")
    SurveySimpleResponseDto toSurveySimpleResponseDto(Survey survey);





}