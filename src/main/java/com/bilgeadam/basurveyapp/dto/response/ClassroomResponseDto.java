package com.bilgeadam.basurveyapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Eralp Nitelik
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ClassroomResponseDto {
    Long classroomOid;
    String classroomName;
    List<UserResponseDto> masterTrainers;
    List<UserResponseDto> assistantTrainers;
    List<UserSimpleResponseDto> students;
    List<SurveyResponseDto> surveys;
}
