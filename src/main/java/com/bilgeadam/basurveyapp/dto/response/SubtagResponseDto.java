package com.bilgeadam.basurveyapp.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SubtagResponseDto {
    Long subTagStringId;
    @NotNull
    String subTagString;
}
