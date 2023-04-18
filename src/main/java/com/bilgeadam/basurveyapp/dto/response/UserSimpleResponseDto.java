package com.bilgeadam.basurveyapp.dto.response;

import com.bilgeadam.basurveyapp.entity.Role;
import lombok.*;

import java.util.Set;

/**
 * @author Eralp Nitelik
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserSimpleResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private String authorizedRole;
    private Set<Role> roles;
}
