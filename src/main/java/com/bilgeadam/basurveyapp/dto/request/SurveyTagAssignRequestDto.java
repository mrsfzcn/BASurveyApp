package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyTagAssignRequestDto {
    @NotNull
    private List<Long> surveyTagOid;
    @NotNull
    private Long surveyOid;
}
