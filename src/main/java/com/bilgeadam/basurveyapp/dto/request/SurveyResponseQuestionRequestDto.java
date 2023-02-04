package com.bilgeadam.basurveyapp.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SurveyResponseQuestionRequestDto {

    private Map<Long,String> createResponses;
}