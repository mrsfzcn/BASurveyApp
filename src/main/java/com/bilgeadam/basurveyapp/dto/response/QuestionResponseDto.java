package com.bilgeadam.basurveyapp.dto.response;


import com.bilgeadam.basurveyapp.entity.Question;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionResponseDto {
    Long questionOid;
    String questionString;
    Long questionTypeOid;
    Integer order;
    List<QuestionTagResponseDto> tagOids;

}
