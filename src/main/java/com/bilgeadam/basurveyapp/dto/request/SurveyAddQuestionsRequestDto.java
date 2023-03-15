package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyAddQuestionsRequestDto {
    @NotNull
    private Set<Long> questionIds;
    @NotNull
    private Long surveyId;

}
