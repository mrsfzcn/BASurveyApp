package com.bilgeadam.basurveyapp.dto.request;

import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
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
public class CreateStudentTagRequestDto {

    String tagString;
}
