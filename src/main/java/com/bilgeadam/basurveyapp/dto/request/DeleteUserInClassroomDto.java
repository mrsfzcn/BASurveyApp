package com.bilgeadam.basurveyapp.dto.request;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class DeleteUserInClassroomDto {

    private Long classroomOid;
    private String userEmail;


}
