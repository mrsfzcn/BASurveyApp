package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyResponseQuestionRequestDto {
    @NotNull
    Long questionOid;
    @NotNull
    @NotBlank
    String responseString;
}