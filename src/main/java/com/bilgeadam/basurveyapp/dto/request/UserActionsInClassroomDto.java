package com.bilgeadam.basurveyapp.dto.request;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class UserActionsInClassroomDto {

    private Long classroomOid;
    private String userEmail;


}
