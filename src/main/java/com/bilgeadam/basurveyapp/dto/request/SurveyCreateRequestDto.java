package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyCreateRequestDto {
    @NotBlank
    @NotNull
    private String surveyTitle;
    @NotBlank
    @NotNull
    private String courseTopic;

    private List<Long> surveyTagIds;
}