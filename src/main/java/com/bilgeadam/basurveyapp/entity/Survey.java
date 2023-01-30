package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "surveys")
public class Survey extends BaseEntity {
    @Column(name = "survey_title")
    private String surveyTitle;

    @Column(name = "course_topic")
    private String courseTopic;

    @Column(name = "start_Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "classroom_oid")
    private Classroom classroom;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Question> questions;

    @ManyToMany(mappedBy = "surveys", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User> users;
}
