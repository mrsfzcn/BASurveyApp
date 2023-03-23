package com.bilgeadam.basurveyapp.dto.response;

import com.bilgeadam.basurveyapp.entity.Role;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyUserResponseDto {

    private String firstName;
    private String lastName;
    private Set<Role> roles;
}