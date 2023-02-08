package com.bilgeadam.basurveyapp.dto.request;

import lombok.*;

import java.util.List;

/**
 * @author Eralp Nitelik
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class AddUsersToClassroomDto {
    private String classroomName;
    private List<String> userEmails;

}
