package com.bilgeadam.basurveyapp.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VerifyCodeRequestDto {

    private String twoFactoryKey;

    private Long id;
}
