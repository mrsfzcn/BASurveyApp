package com.bilgeadam.basurveyapp.dto.request;

import com.bilgeadam.basurveyapp.entity.Question;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class SurveyCreateRequestDto {
    private String surveyTitle;
    private List<Question> questions;
    private String courseTopic;
}