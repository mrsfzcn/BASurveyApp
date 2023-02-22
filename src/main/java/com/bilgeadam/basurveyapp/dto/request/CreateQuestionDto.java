package com.bilgeadam.basurveyapp.dto.request;

import com.bilgeadam.basurveyapp.entity.SubTag;
import com.bilgeadam.basurveyapp.entity.Tag;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @NotBlank
    @NotNull
    Long surveyOid;
    @NotBlank
    @NotNull
    Long questionTypeOid;
    Integer order;
    Tag tag;
    List<SubTag> subTags;

}
