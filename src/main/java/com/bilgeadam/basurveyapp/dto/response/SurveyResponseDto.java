package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class SurveyResponseDto {
    private Long surveyOid;
    private String surveyTitle;
    private String courseTopic;
}