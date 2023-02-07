package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class AllQuestionResponseDto {
    Long questionOid;
    String questionString;
    Integer order;
}
