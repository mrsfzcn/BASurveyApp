package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateQuestionDto {
    @NotBlank
    @NotNull
    Long questionOid;
    @NotBlank
    @NotNull
    String questionString;
    List<Long> tagOids;
    List<Long> subTagOids;

}
