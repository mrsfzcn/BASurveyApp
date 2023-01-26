package com.bilgeadam.basurveyapp.dto.request;

import com.bilgeadam.basurveyapp.entity.Question;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class SurveyCreateRequestDto {
    private String surveyTitle;
    private Date startDate;
    private Date endDate;
    private Long classroomId;
    private List<Question> questions;
    private String courseTopic;
}