package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

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
