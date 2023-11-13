package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import com.bilgeadam.basurveyapp.entity.tags.TrainerTag;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "trainers")
public class Trainer extends BaseEntity {
    @OneToOne
    User user;
    @OneToMany(mappedBy = "whoCreatedSurvey", fetch = FetchType.EAGER)
    Set<Survey> surveysCreatedByTrainer;
    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "targetEntities", fetch = FetchType.EAGER)
    Set<TrainerTag> trainerTags;
    boolean isMasterTrainer;

    @ManyToOne
    Branch branch;
}
