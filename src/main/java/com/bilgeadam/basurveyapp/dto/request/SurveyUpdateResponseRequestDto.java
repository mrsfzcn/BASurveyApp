package com.bilgeadam.basurveyapp.dto.request;

import lombok.Getter;

import java.util.Map;

@Getter
public class SurveyUpdateResponseRequestDto {

    private Map<Long,String> updateAnswerMap;
}