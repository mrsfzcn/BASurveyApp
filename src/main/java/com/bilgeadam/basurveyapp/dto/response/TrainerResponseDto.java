package com.bilgeadam.basurveyapp.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TrainerResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private List<TrainerTagResponseDto> trainerTags;
}
