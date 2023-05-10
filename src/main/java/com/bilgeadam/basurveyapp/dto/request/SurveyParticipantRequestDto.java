package com.bilgeadam.basurveyapp.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyParticipantRequestDto {

    private Long surveyOid;
    private Long studentTagOid;
}
