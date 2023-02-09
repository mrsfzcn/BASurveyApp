package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateQuestionDto {
    @NotBlank
    @NotNull
    String questionString;
    @NotBlank
    @NotNull
    Long surveyOid;
    @NotBlank
    @NotNull
    Long questionTypeOid;
    Integer order;
}
