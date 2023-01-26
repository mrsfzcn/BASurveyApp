package com.bilgeadam.basurveyapp.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SurveyUpdateRequestDto {
    private String surveyTitle;
}
