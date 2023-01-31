package com.bilgeadam.basurveyapp.dto.request;

import lombok.Getter;

import java.util.Map;

@Getter
public class SurveyResponseQuestionRequestDto {

    private Map<Long,String> responses;
}