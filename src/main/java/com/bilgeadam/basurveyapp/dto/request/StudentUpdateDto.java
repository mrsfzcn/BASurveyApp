package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StudentUpdateDto {
    @NotBlank
    @NotNull
    private Long studentTagOid;
    @NotBlank
    @NotNull
    private Long studentOid;

}
