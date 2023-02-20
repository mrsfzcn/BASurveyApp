package com.bilgeadam.basurveyapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyByClassroomQuestionsResponseDto {
    private Long questionOid;
    private String questionString;
    private List<SurveyByClassroomQuestionAnswersResponseDto> responseDtoList;
}
