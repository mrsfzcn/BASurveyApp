package com.bilgeadam.basurveyapp.mapper;


import com.bilgeadam.basurveyapp.dto.request.SurveyAssignRequestDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyCreateRequestDto;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.entity.SurveyRegistration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)

public interface SurveyMapper {
    SurveyMapper INSTANCE = Mappers.getMapper(SurveyMapper.class);

    List<SurveyByStudentTagResponseDto> toSurveyByStudentTagResponseDtoList(List<Survey> surveys);
    List<SurveyResponseDto> toSurveyResponseDtoList(List<Survey> surveys);
    Survey toSurvey(SurveyCreateRequestDto surveyCreateRequestDto);
    Set<SurveySimpleResponseDto> toSurveySimpleResponseDtoSet(Set<Survey> surveySet);
    SurveyRegistration toSurveyRegistration(SurveyAssignRequestDto surveyCreateRequestDto, Survey survey, Long studentTagId, LocalDateTime startDate, LocalDateTime endDate);
    TrainerClassroomSurveyResponseDto toTrainerClassroomSurveyResponseDto(User user, Set<SurveySimpleResponseDto> surveysByThisTrainer);
    SurveyResponseWithAnswersDto toSurveyResponseWithAnswersDto(Survey survey, List<QuestionWithAnswersResponseDto> surveyAnswers);

    SurveyOfClassroomMaskedResponseDto toSurveyOfClassroomMaskedResponseDto(Survey survey, List<QuestionWithAnswersMaskedResponseDto> surveyAnswers);
    @Mapping(source = "oid", target = "questionOid")
    @Mapping(source = "questionString", target = "questionString")
    @Mapping(source = "questionType.oid", target = "questionTypeOid")
    @Mapping(source = "order", target = "order")
    QuestionWithAnswersResponseDto toQuestionWithAnswersResponseDto(Question question, List<ResponseUnmaskedDto> responses);


    @Mapping(source = "oid", target = "questionOid")
    @Mapping(source = "questionType.oid", target = "questionTypeOid")
    @Mapping(target = "responses", expression = "java(mapResponses(question, usersInClassroom))")
    QuestionWithAnswersMaskedResponseDto toQuestionWithAnswersMaskedResponseDto(Question question, List<Long> usersInClassroom);

    List<ResponseUnmaskedDto> mapResponses(Question question, List<Long> usersInClassroom);

    default ResponseUnmaskedDto toResponseUnmaskedDto(User user, ResponseUnmaskedDto responseString) {
        ResponseUnmaskedDto unmaskedDto = new ResponseUnmaskedDto();
        unmaskedDto.setUserOid(user.getOid());
        unmaskedDto.setResponse(responseString.getResponse());
        return unmaskedDto;
    }
}