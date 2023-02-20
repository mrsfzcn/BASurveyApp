package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Eralp Nitelik
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyAssignRequestDto {
    @NotBlank
    @NotNull
    Long surveyId;
    @NotBlank
    @NotNull
    String classroomName;
    @NotBlank
    @NotNull
    Integer days;
    @NotBlank
    @NotNull
    private String startDate;
}
