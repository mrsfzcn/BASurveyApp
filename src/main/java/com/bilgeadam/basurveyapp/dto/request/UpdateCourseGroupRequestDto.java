package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCourseGroupRequestDto {

    @NotBlank
    private String apiId;
    @NotBlank
    private String name;
    @NotBlank
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @NotBlank
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    @NotBlank
    private Long courseId;
    @NotBlank
    private Long branchId;
    @NotBlank
    private List<Long> trainers;
}
