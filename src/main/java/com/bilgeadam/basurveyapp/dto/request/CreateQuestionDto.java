package com.bilgeadam.basurveyapp.dto.request;

import com.bilgeadam.basurveyapp.entity.QuestionType;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.entity.baseentity.BaseEntity;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class CreateQuestionDto extends BaseEntity {

    String questionString;
    Survey surveyOid;
    QuestionType questionTypeOid;
    Integer order;


}
