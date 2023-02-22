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
public class ChangeLoginRequestDto {
    @Size(min = 3, message = "Email must be valid.")
    @Email(message = "Email must be valid.")
    @NotBlank(message = "Email must be valid.")
    @NotNull(message = "Email must be valid.")
    private String userEmail;

    @NotBlank(message = "Token must be valid.")
    @NotNull(message = "Token must be valid.")
    private String authorizedToken;
}
