package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @author Eralp Nitelik
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FindAllResponsesOfUserRequestDto {
    @NotBlank
    @NotNull
    String userEmail;
    @NotBlank
    @NotNull
    Long surveyOid;
}
