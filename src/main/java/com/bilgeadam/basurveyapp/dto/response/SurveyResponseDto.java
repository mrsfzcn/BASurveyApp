package com.bilgeadam.basurveyapp.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyResponseDto {
    private Long surveyOid;
    private String surveyTitle;
    private String courseTopic;

    private List<SurveyClassroomResponseDto> surveyClassroomResponseDtoList;
    private List<SurveyQuestionResponseDto> surveyQuestionResponseDtoList;
    private List<SurveyUserResponseDto> surveyUserResponseDtoList;
}