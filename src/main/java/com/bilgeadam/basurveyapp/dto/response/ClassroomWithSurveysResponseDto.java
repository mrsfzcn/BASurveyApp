package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

import java.util.List;

/**
 * @author Eralp Nitelik
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ClassroomWithSurveysResponseDto {
    Long classroomOid;
    String classroomName;
    List<SurveyResponseDto> surveys;
}
