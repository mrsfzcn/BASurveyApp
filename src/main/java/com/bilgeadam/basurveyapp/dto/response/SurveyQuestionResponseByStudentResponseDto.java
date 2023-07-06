package com.bilgeadam.basurveyapp.dto.response;

import jakarta.persistence.Column;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyQuestionResponseByStudentResponseDto {
    /**QuestionSide
     */
    String questionString;
    /** ResponseSide
     */
    List<String> responseString;
    /**StudentInfoSide
     */
    List<String> studentNames;
}
