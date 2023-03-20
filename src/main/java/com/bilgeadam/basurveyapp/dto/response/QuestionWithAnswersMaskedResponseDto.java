package com.bilgeadam.basurveyapp.dto.response;

import com.bilgeadam.basurveyapp.entity.Response;
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
public class QuestionWithAnswersMaskedResponseDto {
    Long questionOid;
    String questionString;
    Long questionTypeOid;
    Integer order;
    List<Response> responses;
}
