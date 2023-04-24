package com.bilgeadam.basurveyapp.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetTrainerTagsByEmailResponse {

    List<String> trainerTags;
}
