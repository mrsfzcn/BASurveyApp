package com.bilgeadam.basurveyapp.dto.request;

import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @author Eralp Nitelik
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyAssignRequestDto {
    @NotNull
    Long surveyId;
    @NotBlank
    @NotNull
    String studentTag;

    @NotNull
    Integer days;
    private String startDate;


}
