package com.bilgeadam.basurveyapp.dto.request;

import com.bilgeadam.basurveyapp.dto.response.QuestionOrderResponseDto;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyAddQuestionsRequestDto {
    @NotNull
    private List<QuestionOrderResponseDto> questionIds;
    @NotNull
    private Long surveyId;

}
