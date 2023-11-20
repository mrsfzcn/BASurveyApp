package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import com.bilgeadam.basurveyapp.entity.tags.SurveyTag;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "surveys")
//TODO geçici yazıldı. Daha iyi bir çözüm üretilecek. --> (bug olarak board' a eklendi)
@JsonIgnoreProperties({"surveyRegistrations", "studentsWhoAnswered", "questions", "surveyTags", "responses"})

public class Survey extends BaseEntity {

    @Column(name = "survey_title", unique = true)
    private String surveyTitle;

    @Column(name = "course_topic")
    private String courseTopic;

    @JsonManagedReference
    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SurveyRegistration> surveyRegistrations;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Student> studentsWhoAnswered;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Student> studentsWhoDidntAnswered;

    @JsonManagedReference
    @ManyToMany(mappedBy = "surveys", fetch = FetchType.LAZY)
    private List<Question> questions;

    @ManyToOne(fetch = FetchType.LAZY)
    private Trainer whoCreatedSurvey;

    @ManyToMany(mappedBy = "targetEntities", fetch = FetchType.LAZY)
    private Set<SurveyTag> surveyTags;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Response> responses;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SurveyQuestionOrder> surveyQuestionOrders;

    @ElementCollection
    private List<Long> requiredQuestionIndexes;
}
