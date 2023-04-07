package com.bilgeadam.basurveyapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MasterTrainerResponseDto {
    private Long oid;
    private String firstName;
    private String lastName;
    private String email;
    private List<MasterTrainerTagResponseDto> trainerTags;
}
