package com.bilgeadam.basurveyapp.dto.request;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class UpdateQuestionDto {
    Long questionOid;
    String questionString;
}
