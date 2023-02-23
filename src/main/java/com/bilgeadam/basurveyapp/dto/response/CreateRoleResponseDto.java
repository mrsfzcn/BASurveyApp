package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

/**
 * @author Eralp Nitelik
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateRoleResponseDto {
    private Long roleOid;
    private String role;
}
