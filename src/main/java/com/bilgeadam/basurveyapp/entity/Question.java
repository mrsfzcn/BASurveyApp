package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.baseentity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "questions")
public class Question extends BaseEntity {
    @Column(name = "question_description")
    private String questionDescription;

    @ManyToOne
    @JoinColumn(referencedColumnName = "oid", name = "survey")
    private Survey survey;

    @ManyToOne
    @JoinColumn(referencedColumnName = "oid", name = "question_type")
    private QuestionType questionType;

    private Integer order;
}
