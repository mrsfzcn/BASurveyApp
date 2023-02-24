package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FilterSurveyQuestionsByKeywordRequestDto {


    @NotNull
    Long surveyOid;
    String keyword;


}
