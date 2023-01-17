package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.baseentity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "question_types")
public class QuestionType extends BaseEntity {
    @Column(name = "question_type")
    private String questionType;
}
