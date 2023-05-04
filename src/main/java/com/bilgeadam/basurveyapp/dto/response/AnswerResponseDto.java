package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AnswerResponseDto {
    private Long userOid;
    private Long questionOid;
    private Long surveyOid;
    private String responseString;
}
