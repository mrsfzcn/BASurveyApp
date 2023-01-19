package com.bilgeadam.basurveyapp.dto.response;

import com.bilgeadam.basurveyapp.entity.QuestionType;
import com.bilgeadam.basurveyapp.entity.Survey;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class QuestionFindByIdResponseDto {
    String questionString;
    Integer order;
    Survey surveyId;
    QuestionType questionTypeId;
}
