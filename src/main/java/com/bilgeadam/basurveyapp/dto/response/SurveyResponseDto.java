package com.bilgeadam.basurveyapp.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SurveyResponseDto {

    private String surveyTitle;
    private String courseTopic;
    private Date startDate;
    private Date endDate;
    private List<SurveyClassroomResponseDto> classroomResponseDtos;
    private List<SurveyQuestionResponseDto> questionResponseDtos;
    private List<SurveyUserResponseDto> userResponseDtos;
}