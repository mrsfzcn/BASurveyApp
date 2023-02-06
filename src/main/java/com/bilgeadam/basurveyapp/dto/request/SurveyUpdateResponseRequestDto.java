package com.bilgeadam.basurveyapp.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SurveyUpdateResponseRequestDto {

    private Map<Long,String> updateResponseMap;
}