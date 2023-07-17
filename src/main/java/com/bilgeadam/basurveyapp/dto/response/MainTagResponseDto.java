package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MainTagResponseDto {
    String tagClass;
    String tagName;
}
