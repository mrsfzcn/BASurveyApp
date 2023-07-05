package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyTagAssignRequestDto {


    @NotNull
    private Long surveyTagOid;

    @NotNull
    private Long surveyOid;
}
