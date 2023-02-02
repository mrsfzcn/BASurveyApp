package com.bilgeadam.basurveyapp.dto.request;

import com.bilgeadam.basurveyapp.entity.Question;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResponseRequestSaveDto {

    String responseString;
    Long questionOid;
}
