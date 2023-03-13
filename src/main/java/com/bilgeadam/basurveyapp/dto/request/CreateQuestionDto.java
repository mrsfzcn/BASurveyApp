package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateQuestionDto {
    @NotBlank
    @NotNull
    String questionString;

    @NotNull
    Long questionTypeOid;
    Integer order;
    Set<Long> tagOids;

}
