package com.bilgeadam.basurveyapp.dto.request;

import com.bilgeadam.basurveyapp.entity.SubTag;
import com.bilgeadam.basurveyapp.entity.Tag;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateQuestionDto {
    @NotBlank
    @NotNull
    String questionString;

    @NotNull
    Long questionTypeOid;
    Integer order;
    List<Long> tagOids;
    List<Long> subTagOids;

}
