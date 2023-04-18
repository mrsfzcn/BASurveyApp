package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
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
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private QuestionType questionType;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Response> responses;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Survey> surveys;
    @ManyToMany(mappedBy = "targetEntities", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<QuestionTag> questionTag;
}
