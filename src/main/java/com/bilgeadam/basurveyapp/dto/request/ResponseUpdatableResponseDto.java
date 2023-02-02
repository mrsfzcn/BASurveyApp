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
public class ResponseUpdatableResponseDto {
    private Long oid;
    private String responseString;
    private Long questionOid;
    private Long userOid;
}
