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
    @NotNull
    Long questionOid;
    List<Long> tagOids;
    @NotBlank
    @NotNull
    String questionString;

}
