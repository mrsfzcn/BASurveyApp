package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyResponseDto {
    private Long surveyOid;
    private String surveyTitle;
    private String courseTopic;

//    private List<SurveyClassroomResponseDto> surveyClassroomResponseDtoList;
//    private List<SurveyQuestionResponseDto> surveyQuestionResponseDtoList;
//    private List<SurveyUserResponseDto> surveyUserResponseDtoList;
}