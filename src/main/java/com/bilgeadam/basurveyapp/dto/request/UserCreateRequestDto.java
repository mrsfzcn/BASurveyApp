package com.bilgeadam.basurveyapp.dto.request;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserCreateRequestDto {

    private String firstName;
    private String lastName;
    private String role;
    private String email;
    private String password;
    private String classroomName;
}