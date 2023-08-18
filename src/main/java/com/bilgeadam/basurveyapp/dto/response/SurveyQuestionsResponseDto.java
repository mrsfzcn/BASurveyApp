package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyQuestionsResponseDto {
    private Long questionIds;
    private String questionString;
    private String questionType;
}
