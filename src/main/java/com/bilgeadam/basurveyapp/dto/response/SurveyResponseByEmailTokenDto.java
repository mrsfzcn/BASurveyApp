package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyResponseByEmailTokenDto {
    private Long oid;
    private String surveyTitle;
    private String courseTopic;
    private List<SurveyClassroomResponseDto> surveyTags;
    private List<SurveyQuestionResponseDto> questions;
    private List<Long> requiredQuestionIndexes;
}
