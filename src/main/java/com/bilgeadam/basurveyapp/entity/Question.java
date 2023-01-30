package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
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
    @Column(name = "question_string")
    private String questionString;

    @ManyToOne
    @JoinColumn(referencedColumnName = "oid", name = "survey")
    private Survey survey;

    @ManyToOne
    @JoinColumn(referencedColumnName = "oid", name = "question_type")
    private QuestionType questionType;

    @Column(name = "question_order")
    private Integer order;
}
