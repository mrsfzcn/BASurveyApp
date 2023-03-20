package com.bilgeadam.basurveyapp.entity.tags;

import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.Trainer;
import com.bilgeadam.basurveyapp.entity.base.BaseTag;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "trainertags")
public class TrainerTag extends BaseTag<Trainer> {

}
