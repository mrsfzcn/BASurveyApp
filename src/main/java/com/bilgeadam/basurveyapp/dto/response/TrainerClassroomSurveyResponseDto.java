package com.bilgeadam.basurveyapp.dto.response;

import com.bilgeadam.basurveyapp.entity.Survey;
import lombok.*;

import java.util.List;
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
    Set<Survey> surveysByThisTrainer;
}
