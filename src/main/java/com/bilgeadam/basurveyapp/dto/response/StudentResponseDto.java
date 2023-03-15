package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StudentResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private List<StudentTagResponseDto> studentTags;
}