package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserCreateRequestDto {
    @NotBlank
    @NotNull
    private String firstName;
    @NotBlank
    @NotNull
    private String lastName;
    @NotBlank
    @NotNull
    private String role;
    @NotBlank
    @NotNull
    private String email;
    @NotBlank
    @NotNull
    private String password;
    @NotBlank
    @NotNull
    private String classroomName;
}