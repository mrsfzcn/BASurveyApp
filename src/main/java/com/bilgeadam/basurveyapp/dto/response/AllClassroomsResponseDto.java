package com.bilgeadam.basurveyapp.dto.response;

import com.bilgeadam.basurveyapp.entity.User;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class AllClassroomsResponseDto {
    String name;
    // TODO fix return type
    List<User> users;
}
