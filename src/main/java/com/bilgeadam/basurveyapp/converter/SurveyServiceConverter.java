package com.bilgeadam.basurveyapp.converter;

import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.Survey;

import java.util.List;

public class SurveyServiceConverter {

    public static QuestionWithAnswersMaskedResponseDto convertToQuestionWithAnswersMaskedResponseDto(Question question, List<SimpleResponseDto> responseDtos) {
        return QuestionWithAnswersMaskedResponseDto.builder()
                .questionOid(question.getOid())
                .questionString(question.getQuestionString())
                .questionTypeOid(question.getQuestionType().getOid())
                .order(question.getOrder())
                .responses(responseDtos)
                .build();
    }

    public static QuestionWithAnswersResponseDto convertToQuestionWithAnswersResponseDto(Question question, List<ResponseUnmaskedDto> responseDtos) {
        return QuestionWithAnswersResponseDto.builder()
                .questionOid(question.getOid())
                .questionString(question.getQuestionString())
                .questionTypeOid(question.getQuestionType().getOid())
                .order(question.getOrder())
                .responses(responseDtos)
                .build();
    }

    public static SurveyOfClassroomMaskedResponseDto convertToSurveyOfClassroomMaskedResponseDto(Survey survey, List<QuestionWithAnswersMaskedResponseDto> questions) {
        return SurveyOfClassroomMaskedResponseDto.builder()
                .surveyOid(survey.getOid())
                .surveyTitle(survey.getSurveyTitle())
                .courseTopic(survey.getCourseTopic())
                .questions(questions)
                .build();
    }

    public static SurveyResponseWithAnswersDto convertToSurveyResponseWithAnswersDto(Survey survey, List<QuestionWithAnswersResponseDto> questions) {
        return SurveyResponseWithAnswersDto.builder()
                .surveyOid(survey.getOid())
                .surveyTitle(survey.getSurveyTitle())
                .courseTopic(survey.getCourseTopic())
                .questions(questions)
                .build();
    }


}
