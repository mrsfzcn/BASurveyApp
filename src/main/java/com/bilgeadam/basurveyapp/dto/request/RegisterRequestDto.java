package com.bilgeadam.basurveyapp.dto.request;

import com.bilgeadam.basurveyapp.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Eralp Nitelik
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDto {
    @Size(min = 3, message = "Email must be valid.")
    @Email(message = "Email must be valid.", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @NotBlank(message = "Email must be valid.")
    @NotNull(message = "Email must be valid.")
    private String email;

    @Size(min = 8, max = 256, message = "Password should have at least 8 characters")
    @NotBlank(message = "Password must be valid.")
    @NotNull(message = "Password must be valid.")
    private String password;

    // all the fields that you might need when registering...
    @NotNull
    @NotBlank
    @Size(min = 2, max = 128)
    private String firstName;
    @NotNull
    @NotBlank
    @Size(min = 2, max = 128)
    private String lastName;
    @NotNull
    private Role role;
}
