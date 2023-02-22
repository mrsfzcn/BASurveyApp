package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToMany(mappedBy = "surveys", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Question> questions;

    @ManyToMany(mappedBy = "surveys", fetch = FetchType.LAZY)
    private List<User> users;
}
