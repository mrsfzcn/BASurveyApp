package com.bilgeadam.basurveyapp.mapper;


import com.bilgeadam.basurveyapp.dto.request.SurveyAssignRequestAdapter;
import com.bilgeadam.basurveyapp.dto.request.SurveyAssignRequestDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyCreateRequestDto;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.*;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.repositories.StudentTagRepository;
import com.bilgeadam.basurveyapp.services.StudentTagService;
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

    List<SurveyByStudentTagResponseDto> toSurveyByStudentTagResponseDtoList(List<Survey> surveys);
    List<SurveyResponseDto> toSurveyResponseDtoList(List<Survey> surveys);
    Survey toSurvey(SurveyCreateRequestDto surveyCreateRequestDto);
    Set<SurveySimpleResponseDto> toSurveySimpleResponseDtoSet(Set<Survey> surveySet);
    TrainerClassroomSurveyResponseDto toTrainerClassroomSurveyResponseDto(User user, Set<SurveySimpleResponseDto> surveysByThisTrainer);
    SurveyResponseWithAnswersDto toSurveyResponseWithAnswersDto(Survey survey, List<QuestionWithAnswersResponseDto> surveyAnswers);

    SurveyOfClassroomMaskedResponseDto toSurveyOfClassroomMaskedResponseDto(Survey survey, List<QuestionWithAnswersMaskedResponseDto> surveyAnswers);
    @Mapping(source = "question.oid", target = "questionOid")
    @Mapping(source = "question.questionString", target = "questionString")
    @Mapping(source = "question.questionType.oid", target = "questionTypeOid")
    @Mapping(source = "question.order", target = "order")
    @Mapping(source = "responseUnmaskedDtos", target = "responses")
    QuestionWithAnswersResponseDto toQuestionWithAnswersResponseDto(Question question, List<ResponseUnmaskedDto> responseUnmaskedDtos);


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