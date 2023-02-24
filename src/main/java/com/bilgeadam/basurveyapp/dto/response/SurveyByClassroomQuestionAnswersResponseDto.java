package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyByClassroomQuestionAnswersResponseDto {
    private Long responseOid;
    private String responseString;
}
