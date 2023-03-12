package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
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

}
