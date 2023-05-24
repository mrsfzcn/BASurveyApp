package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private QuestionType questionType;
    @JsonBackReference
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Response> responses;

    @JsonBackReference
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Survey> surveys;
    @JsonBackReference
    @ManyToMany(mappedBy = "targetEntities", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<QuestionTag> questionTag;
}
