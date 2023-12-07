package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class StudentResponseDto {
    private Long oid;
    private String firstName;
    private String lastName;
    private String email;
    private String createdAt;
    private List<StudentTagResponseDto> studentTags;
}