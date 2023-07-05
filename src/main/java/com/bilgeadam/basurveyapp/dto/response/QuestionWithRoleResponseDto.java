package com.bilgeadam.basurveyapp.dto.response;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionWithRoleResponseDto {
    Long questionOid;
    String questionString;
    Long questionTypeOid;

}
