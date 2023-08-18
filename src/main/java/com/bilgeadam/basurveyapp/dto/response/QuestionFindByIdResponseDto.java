package com.bilgeadam.basurveyapp.dto.response;

import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionFindByIdResponseDto {

    Long questionOid;
    String questionString;
    Integer order;
    List<SurveySimpleResponseDto> surveys;
    String questionType;
//    List<String> questionTags;
//    List<Long> questionTagOid;
    List<TagResponseDto> questionTags;

    // List<Dto>
    // questionTagOid
    // questionTags
}
