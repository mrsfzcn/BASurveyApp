package com.bilgeadam.basurveyapp.dto.response;


import lombok.*;



@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class AllClassroomsResponseDto {
    Long oid;
    String name;

}
