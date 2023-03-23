package com.bilgeadam.basurveyapp.mapper;


import com.bilgeadam.basurveyapp.dto.request.SurveyAssignRequestDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyCreateRequestDto;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.*;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.entity.tags.SurveyTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)

public interface SurveyMapper {
    SurveyMapper INSTANCE = Mappers.getMapper(SurveyMapper.class);

    List<SurveyByStudentTagResponseDto> toSurveyByStudentTagResponseDtoList(final List<Survey> surveys);
    Survey toSurvey(final SurveyCreateRequestDto surveyCreateRequestDto);
    @Mapping(target = "surveyOid", source = "oid")
    SurveySimpleResponseDto toSurveySimpleResponseDto(final Survey survey);
    List<SurveyResponseDto> toSurveyResponseDtoList(final List<Survey> surveys);
    @Mapping(source = "tagString", target = "name")
    SurveyClassroomResponseDto toSurveyClassroomResponseDto(final SurveyTag surveyTag);
    List<SurveyClassroomResponseDto> toSurveyClassroomResponseDto(final List<SurveyTag> surveyTags);
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "firstName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    SurveyStudentResponseDto toSurveyStudentResponseDto(final Student student);
    List<SurveyStudentResponseDto> toSurveyStudentResponseDto(final List<Student> studentsWhoAnswered);



    TrainerClassroomSurveyResponseDto toTrainerClassroomSurveyResponseDto(final Trainer trainer);
    Set<SurveySimpleResponseDto> toSurveySimpleResponseDtoSet(final Set<Survey> surveySet);
    SurveyRegistration toSurveyRegistration(final SurveyAssignRequestDto surveyCreateRequestDto, Survey survey, Long studentTagId, LocalDateTime startDate, LocalDateTime endDate);


    @Mapping(source = "oid", target = "surveyOid")
    SurveyResponseWithAnswersDto toSurveyResponseWithAnswersDto(final Survey survey);

    @Mapping(source = "questionType.questionType", target = "questionType")
    SurveyQuestionResponseDto toSurveyQuestionResponseDto(final Question question);
    List<SurveyQuestionResponseDto> toSurveyQuestionResponseDto(final List<Question> question);

    List<QuestionWithAnswersResponseDto> toQuestionWithAnswersResponseDto(final List<Question> question);

    @Mapping(source = "survey.oid", target = "surveyOid")
    SurveyOfClassroomMaskedResponseDto toSurveyOfClassroomMaskedResponseDto(final Survey survey);
    @Mapping(source = "oid", target = "questionOid")
    @Mapping(source = "questionType.oid", target = "questionTypeOid")
    QuestionWithAnswersMaskedResponseDto toQuestionWithAnswersMaskedResponseDto(final Question question);
    List<QuestionWithAnswersMaskedResponseDto> toQuestionWithAnswersMaskedResponseDto(final List<Response> response);
    @Mapping(source = "oid", target = "questionOid")
    @Mapping(source = "questionString", target = "questionString")
    @Mapping(source = "questionType.oid", target = "questionTypeOid")
    @Mapping(source = "order", target = "order")
    QuestionWithAnswersResponseDto toQuestionWithAnswersResponseDto(final Question question);

    @Mapping(source = "oid", target = "responseOid")
    @Mapping(source = "user.oid", target = "userOid")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    ResponseUnmaskedDto toResponseUnmaskedDto(final Response response);



    List<ResponseUnmaskedDto> toResponseUnmaskedDto(final List<Response> responses);

    @Mapping(source = "question.oid", target = "questionOid")
    @Mapping(source = "question.questionType.oid", target = "questionTypeOid")
    @Mapping(target = "question.responses", expression = "java(mapResponses(question, usersInClassroom))")
    QuestionWithAnswersMaskedResponseDto toQuestionWithAnswersMaskedResponseDto(Question question, List<Long> usersInClassroom);

    default List<ResponseUnmaskedDto> mapResponses(Question question, List<Long> usersInClassroom) {
        List<ResponseUnmaskedDto> responseUnmaskedDtos = new ArrayList<>();
        for (Response response : question.getResponses()) {
            if (usersInClassroom.contains(response.getUser().getOid())) {
                responseUnmaskedDtos.add(toResponseUnmaskedDto(response.getUser(), response.getResponseString()));
            }
        }
        return responseUnmaskedDtos;
    }

    default ResponseUnmaskedDto toResponseUnmaskedDto(User user, String responseString) {
        ResponseUnmaskedDto unmaskedDto = new ResponseUnmaskedDto();
        unmaskedDto.setUserOid(user.getOid());
        unmaskedDto.setResponse(responseString);
        return unmaskedDto;
    }
}