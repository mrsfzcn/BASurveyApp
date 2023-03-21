package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * @author Taha Yasin CINAR
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AssignRoleToUserRequestDto {
    @NotBlank(message = "Authorized Role must be valid.")
    @NotNull(message = "Authorized Role must be valid.")
    private String role;

    @Size(min = 3, message = "Email must be valid.")
    @Email(message = "Email must be valid.")
    @NotBlank(message = "Email must be valid.")
    @NotNull(message = "Email must be valid.")
    private String userEmail;
}
