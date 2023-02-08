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
@Table(name = "surveys")
public class Survey extends BaseEntity {

    @Column(name = "survey_title", unique = true)
    private String surveyTitle;

    @Column(name = "course_topic")
    private String courseTopic;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "surveys_classrooms",
        joinColumns = @JoinColumn(name = "survey_oid"),
        inverseJoinColumns = @JoinColumn(name = "classroom_oid"))
    private List<Classroom> classrooms;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Question> questions;

    @ManyToMany(mappedBy = "surveys", fetch = FetchType.LAZY)
    private List<User> users;
}
