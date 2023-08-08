package com.bilgeadam.basurveyapp.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegenerateQrCodeRequest {

    private String token;
}
