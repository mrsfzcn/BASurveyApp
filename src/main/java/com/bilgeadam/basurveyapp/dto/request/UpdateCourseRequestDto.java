package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCourseRequestDto {

    @NotBlank
    private String apiId;

    @NotBlank
    private String name;
}
