package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionFindByIdResponseDto {
    String questionString;
    Integer order;
    List<SurveySimpleResponseDto> surveys;
    String questionType;
}
