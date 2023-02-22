package com.bilgeadam.basurveyapp.dto.response;

import com.bilgeadam.basurveyapp.entity.enums.Role;
import lombok.*;

import java.util.List;

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
    private Role role;
}
