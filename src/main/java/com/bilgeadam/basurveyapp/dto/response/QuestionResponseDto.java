package com.bilgeadam.basurveyapp.dto.response;

import com.bilgeadam.basurveyapp.entity.SubTag;
import com.bilgeadam.basurveyapp.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionResponseDto {
    Long questionOid;
    String questionString;
    Integer order;
    List<Long> tagOids;
    List<Long> subTagOids;

}
