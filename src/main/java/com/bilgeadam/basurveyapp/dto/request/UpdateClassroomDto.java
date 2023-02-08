package com.bilgeadam.basurveyapp.dto.request;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class UpdateClassroomDto {
    Long classroomOid;
    Long userOid;
}
