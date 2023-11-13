package com.bilgeadam.basurveyapp.dto.response;

import com.bilgeadam.basurveyapp.entity.enums.ETrainerRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data //@Getter @Setter @ToString hepsini kapsıyor.
@Builder
public class TrainerModelResponseDto {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private ETrainerRole trainerRole;
}
