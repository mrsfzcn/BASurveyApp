package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class SurveyByClassroomQuestionAnswersResponseDto {
    private Long responseOid;
    private String responseString;
}
