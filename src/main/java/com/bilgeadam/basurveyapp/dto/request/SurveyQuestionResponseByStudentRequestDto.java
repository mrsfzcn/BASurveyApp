package com.bilgeadam.basurveyapp.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyQuestionResponseByStudentRequestDto {
    //*sd
    String surveyTitle;
    Long studentTagOId;
}
