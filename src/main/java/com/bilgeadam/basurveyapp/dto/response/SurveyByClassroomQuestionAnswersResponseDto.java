package com.bilgeadam.basurveyapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyByClassroomQuestionAnswersResponseDto {
    private Long responseOid;
    private String responseString;
}