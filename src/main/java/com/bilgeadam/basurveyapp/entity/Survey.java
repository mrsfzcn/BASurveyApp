package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.baseentity.BaseEntity;
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

    @Column(name = "start_Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "classroom_oid")
    private Classroom classroom;

    @Column(name = "course_topic")
    private String courseTopic;

    @OneToMany(mappedBy = "survey")
    private List<Question> question;
}
