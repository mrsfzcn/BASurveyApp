package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class ClassroomUsersResponseDto {
    private String firstName;
    private String lastName;
    private String role;
}
