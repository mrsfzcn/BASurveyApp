package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @Column(name = "question_order")
    private Integer order;
    @ManyToOne
    @JoinColumn(referencedColumnName = "oid", name = "question_type")
    private QuestionType questionType;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Response> responses;
    @ManyToOne
    @JoinColumn(referencedColumnName = "oid", name = "survey")
    private Survey survey;

}
