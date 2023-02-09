package com.bilgeadam.basurveyapp.dto.response;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ClassroomFindByIdResponseDto {
    private String name;
    private List<ClassroomUsersResponseDto> users;

}
