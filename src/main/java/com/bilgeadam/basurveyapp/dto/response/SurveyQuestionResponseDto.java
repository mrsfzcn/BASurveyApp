package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyQuestionResponseDto {

    private Long oid;
    private String questionString;
    private String questionType;
    private List<String> options;
}