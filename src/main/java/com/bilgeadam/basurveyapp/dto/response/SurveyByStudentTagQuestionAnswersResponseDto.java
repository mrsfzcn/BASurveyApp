package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyByStudentTagQuestionAnswersResponseDto {
    private Long responseOid;
    private String responseString;
}
