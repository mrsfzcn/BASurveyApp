package com.bilgeadam.basurveyapp.dto.response;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyResponseDto {
    private Long oid;
    private String surveyTitle;
    private String courseTopic;

    private List<SurveyClassroomResponseDto> surveyTags;
    private List<SurveyQuestionResponseDto> questions;
    private List<SurveyStudentResponseDto> studentsWhoAnswered;
}