package com.bilgeadam.basurveyapp.dto.response;

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

public class UserTrainersAndStudentsResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private Set<UserTrainersAndStudentsRolesResponseDto> roles;

    @Override
    public String toString() {

        return "firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + ", roles=" + roles;
    }
}
