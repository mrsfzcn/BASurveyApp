package com.bilgeadam.basurveyapp.dto.response;

import com.bilgeadam.basurveyapp.entity.tags.SurveyTag;
import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyParticipantResponseDto {

    private String surveyTitle;
    private Set<SurveyTag> surveyTags;
    private Set<ParticipantResponseDto> studentList;
}
