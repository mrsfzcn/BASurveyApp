package com.bilgeadam.basurveyapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StudentModelResponse {

    private Long id;
    private String name;
    private String surname;
    private String personalEmail;
    private String baEmail;
    private String baBoostEmail;
    private Long groupId;
    private Long branchId;

}
