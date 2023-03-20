package com.bilgeadam.basurveyapp.dto.response;

import com.bilgeadam.basurveyapp.entity.Role;
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
    Set<Role> roles;
    Set<SurveySimpleResponseDto> surveysByThisTrainer;
}
