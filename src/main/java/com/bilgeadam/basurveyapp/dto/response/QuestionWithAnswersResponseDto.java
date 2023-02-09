package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

import java.util.List;

/**
 * @author Eralp Nitelik
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionWithAnswersResponseDto {
    Long questionOid;
    String questionString;
    Long questionTypeOid;
    Integer order;
    List<String> responses;
}
