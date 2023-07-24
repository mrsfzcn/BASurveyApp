package com.bilgeadam.basurveyapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindAllUserDetailsResponseDto {

    private Long oid;
    private String firstName;
    private String lastName;
    private String email;
    private String authorizedRole;
    private String createdDate;

}
