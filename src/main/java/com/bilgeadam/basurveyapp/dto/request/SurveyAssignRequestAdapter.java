package com.bilgeadam.basurveyapp.dto.request;

import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyAssignRequestAdapter {
    @NotBlank
    @NotNull
    Long surveyId;
    @NotBlank
    @NotNull
    String studentTag;
    @NotBlank
    @NotNull
    Integer days;
    private String startDate;


}