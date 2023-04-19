package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import com.bilgeadam.basurveyapp.entity.tags.SurveyTag;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Survey extends BaseEntity {

    @Column(name = "survey_title", unique = true)
    private String surveyTitle;

    @Column(name = "course_topic")
    private String courseTopic;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<SurveyRegistration> surveyRegistrations;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Student> studentsWhoAnswered;

    @ManyToMany(mappedBy = "surveys", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Question> questions;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Trainer whoCreatedSurvey;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "targetEntities", fetch = FetchType.EAGER)
    private Set<SurveyTag> surveyTags;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Response> responses;

}
