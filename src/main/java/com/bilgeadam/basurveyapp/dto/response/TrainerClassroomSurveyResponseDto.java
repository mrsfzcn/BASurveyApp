package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

import java.util.Set;

/**
 * @author Eralp Nitelik
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TrainerClassroomSurveyResponseDto {
    String firstName;
    String lastName;
    String email;
    Set<String> roles;
    Set<SurveySimpleResponseDto> surveysByThisTrainer;
}
