package com.bilgeadam.basurveyapp.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateQuestionTypeByTagStringRequestDto {
    private String tagString;
    private String newTagString;
}
