package com.bilgeadam.basurveyapp.dto.request;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class CreateQuestionDto {
    String questionString;
    Long surveyOid;
    Long questionTypeOid;
    Integer order;
}
