package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResponseRequestSaveDto {
    @NotBlank
    @NotNull
    private String responseString;
    @NotBlank
    @NotNull
    private Long questionOid;
}
