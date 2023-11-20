package com.bilgeadam.basurveyapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetRequiredQuestionIndexesDto {

    public Long oid;
    public List<Long> requiredIndexes;
}
