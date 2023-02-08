package com.bilgeadam.basurveyapp.dto.request;

import lombok.*;

/**
 * @author Eralp Nitelik
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class SurveyAssignRequestDto {
    Long surveyId;
    String classroomName;
    Integer days;
}
