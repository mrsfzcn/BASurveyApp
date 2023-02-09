package com.bilgeadam.basurveyapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

/**
 * @author Eralp Nitelik
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddUsersToClassroomDto {
    @NotBlank
    @NotNull
    private String classroomName;
    @NotNull
    private List<String> userEmails;

}
