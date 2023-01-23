package com.bilgeadam.basurveyapp.dto.request;

import com.bilgeadam.basurveyapp.entity.User;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class UpdateClassroomDto {
    Long classroomOid;
    List<User> users;
}
