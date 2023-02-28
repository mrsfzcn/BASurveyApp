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
public class CreateQuestionUserRoleRequestDto {

    @NotBlank
    @NotNull
    String questionString;

    @NotNull
    Long questionTypeOid;
    Integer order;
    List<Long> tagOids;
    List<Long> subTagOids;
    @NotNull
    String role;


}
