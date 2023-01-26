package com.bilgeadam.basurveyapp.dto.request;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class ResponseRequestDto {
    private Long responseOid;
    private String responseString;



}
