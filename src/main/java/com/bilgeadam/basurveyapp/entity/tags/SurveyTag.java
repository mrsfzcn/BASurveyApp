package com.bilgeadam.basurveyapp.entity.tags;

import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.entity.base.BaseTag;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
@EqualsAndHashCode
@Table(name = "surveytag")
public class SurveyTag extends BaseTag<Survey> {
}
