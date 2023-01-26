package com.bilgeadam.basurveyapp.dto.request;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class FindByIdRequestDto {
    Long oid;
}
