package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class SurveySimpleResponseDto {
    private Long surveyOid;
    private String surveyTitle;
    private String courseTopic;
}
