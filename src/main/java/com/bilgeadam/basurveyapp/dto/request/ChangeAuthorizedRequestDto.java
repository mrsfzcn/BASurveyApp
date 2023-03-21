package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * @author Eralp Nitelik
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChangeAuthorizedRequestDto {
    @NotBlank(message = "Authorized Role must be valid.")
    @NotNull(message = "Authorized Role must be valid.")
    private String authorizedRole;

    @NotBlank(message = "Token must be valid.")
    @NotNull(message = "Token must be valid.")
    private String authorizedToken;
}
