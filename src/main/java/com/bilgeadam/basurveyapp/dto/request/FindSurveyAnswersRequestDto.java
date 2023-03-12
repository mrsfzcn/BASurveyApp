package com.bilgeadam.basurveyapp.dto.request;

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
public class FindSurveyAnswersRequestDto {
    @NotNull
    private Long surveyOid;
    @NotNull
    private Long studentTagOid;
}
