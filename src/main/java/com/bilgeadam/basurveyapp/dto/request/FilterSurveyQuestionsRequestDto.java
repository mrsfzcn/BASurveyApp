package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FilterSurveyQuestionsRequestDto {

    @NotNull
    Long surveyOid;
    @NotNull
    List<Long> questionTagOids;



}
