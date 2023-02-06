package com.bilgeadam.basurveyapp.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SurveyQuestionResponseDto {

    private String questionString;
    private Integer order;
    private String questionType;
}