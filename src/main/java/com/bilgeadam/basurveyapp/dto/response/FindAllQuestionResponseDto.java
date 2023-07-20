package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FindAllQuestionResponseDto {
    Long questionOid;
    String questionString;
    String questionType;
    List<String> questionTags;
}
