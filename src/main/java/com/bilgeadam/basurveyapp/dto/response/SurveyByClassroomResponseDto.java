package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class SurveyByClassroomResponseDto {

    private Long surveyOid;
    private String surveyTitle;
    private String courseTopic;

    private List<SurveyByClassroomQuestionsResponseDto> questionDtoList;

}
