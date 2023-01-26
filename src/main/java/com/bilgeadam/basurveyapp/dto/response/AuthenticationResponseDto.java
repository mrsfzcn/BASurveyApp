package com.bilgeadam.basurveyapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Eralp Nitelik
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponseDto {
    private String token;
}
