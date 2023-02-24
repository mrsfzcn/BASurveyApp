package com.bilgeadam.basurveyapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class SurveySimpleResponseDto {
    private Long surveyOid;
    private String surveyTitle;
    private String courseTopic;
}
