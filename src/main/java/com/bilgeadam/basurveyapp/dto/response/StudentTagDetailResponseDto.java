package com.bilgeadam.basurveyapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StudentTagDetailResponseDto {
    private Long oid;
    private String tagString;
}
