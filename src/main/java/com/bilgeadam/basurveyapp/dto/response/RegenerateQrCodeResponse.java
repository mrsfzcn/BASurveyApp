package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegenerateQrCodeResponse {

    private String qrCode;
}
