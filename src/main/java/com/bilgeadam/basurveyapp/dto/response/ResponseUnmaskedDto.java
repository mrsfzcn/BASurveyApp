package com.bilgeadam.basurveyapp.dto.response;

import com.bilgeadam.basurveyapp.entity.Response;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResponseUnmaskedDto {

    Long userOid;
    String firstName;
    String lastName;
    String email;
    Long responseOid;
    String response;


}
