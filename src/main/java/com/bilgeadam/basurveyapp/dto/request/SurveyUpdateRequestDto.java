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
public class SurveyUpdateRequestDto {
    @NotNull
    Long surveyOid;
    @NotBlank
    @NotNull
    private String surveyTitle;
    @NotBlank
    @NotNull
    private String courseTopic;

    private List<Long> surveyTagIds;

}
