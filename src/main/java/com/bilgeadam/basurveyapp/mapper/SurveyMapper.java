package com.bilgeadam.basurveyapp.mapper;


import com.bilgeadam.basurveyapp.dto.response.SurveyByClassroomQuestionAnswersResponseDto;
import com.bilgeadam.basurveyapp.dto.response.SurveyByClassroomQuestionsResponseDto;
import com.bilgeadam.basurveyapp.dto.response.SurveyByClassroomResponseDto;
import com.bilgeadam.basurveyapp.entity.Survey;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;
@Component
public class SurveyMapper {

  public List<SurveyByClassroomResponseDto> mapToSurveyByClassroomResponseDtoList(List<Survey> surveys) {
        return surveys.stream()
                .map(survey -> {
                    SurveyByClassroomResponseDto surveyDto = new SurveyByClassroomResponseDto();
                    surveyDto.setSurveyOid(survey.getOid());
                    surveyDto.setSurveyTitle(survey.getSurveyTitle());
                    surveyDto.setCourseTopic(survey.getCourseTopic());

                    List<SurveyByClassroomQuestionsResponseDto> questionDtoList = survey.getQuestions().stream()
                            .map(question -> {
                                SurveyByClassroomQuestionsResponseDto questionDto = new SurveyByClassroomQuestionsResponseDto();
                                questionDto.setQuestionOid(question.getOid());
                                questionDto.setQuestionString(question.getQuestionString());

                                List<SurveyByClassroomQuestionAnswersResponseDto> responseDtoList = question.getResponses().stream()
                                        .map(answer -> {
                                            SurveyByClassroomQuestionAnswersResponseDto responseDto = new SurveyByClassroomQuestionAnswersResponseDto();
                                            responseDto.setResponseOid(answer.getOid());
                                            responseDto.setResponseString(answer.getResponseString());
                                            return responseDto;
                                        })
                                        .collect(Collectors.toList());

                                questionDto.setResponseDtoList(responseDtoList);
                                return questionDto;
                            })
                            .collect(Collectors.toList());

                    surveyDto.setQuestionDtoList(questionDtoList);
                    return surveyDto;
                })
                .collect(Collectors.toList());
    }
}