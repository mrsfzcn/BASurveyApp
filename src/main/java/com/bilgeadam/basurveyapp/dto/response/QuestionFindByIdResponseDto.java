package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class QuestionFindByIdResponseDto {
    String questionString;
    Integer order;
    String surveyTitle;
    String questionType;
}
