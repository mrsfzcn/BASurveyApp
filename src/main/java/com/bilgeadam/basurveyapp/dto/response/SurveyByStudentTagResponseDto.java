package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyByStudentTagResponseDto {

    private Long surveyOid;
    private String surveyTitle;
    private String courseTopic;

    private List<SurveyByClassroomQuestionsResponseDto> questionDtoList;

}
