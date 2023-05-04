package com.bilgeadam.basurveyapp.dto.response;

import com.bilgeadam.basurveyapp.entity.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QuestionsTrainerTypeResponseDto {
    Long questionOid;
    String questionString;
    Long questionTypeOid;
    Integer order;
    List<QuestionTagResponseDto> questionTags;
    List<ResponseDto> responses;
}
