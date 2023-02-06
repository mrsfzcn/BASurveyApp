package com.bilgeadam.basurveyapp.dto.request;

import lombok.*;

/**
 * @author Eralp Nitelik
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class FindAllResponsesOfUserRequestDto {
    String userEmail;
    Long surveyOid;
}
