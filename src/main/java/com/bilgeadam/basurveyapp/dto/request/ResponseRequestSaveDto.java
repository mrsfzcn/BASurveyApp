package com.bilgeadam.basurveyapp.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResponseRequestSaveDto {

    private String responseString;
    private Long questionOid;
}
