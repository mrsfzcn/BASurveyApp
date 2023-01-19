package com.bilgeadam.basurveyapp.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AllQuestionTypeResponseDto {
    @NotNull
    String questionType;
}
