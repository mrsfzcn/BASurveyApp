package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ParticipantResponseDto {
    private String firstName;
    private String lastName;
    private String email;

}