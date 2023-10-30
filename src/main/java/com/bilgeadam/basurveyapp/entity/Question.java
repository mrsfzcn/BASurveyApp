package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "questions")
public class Question extends BaseEntity {
    // todo : veritabanında 255 karakter ile sınırlı. bu sorun olabilir
    @Column(name = "question_string",length = 10485760)
    private String questionString;
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

    //@Lob
    private List<String> options = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SurveyQuestionOrder> questionSurveys;
}
