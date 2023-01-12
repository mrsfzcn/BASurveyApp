package com.bilgeadam.basurveyapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "surveys")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "oid", nullable = false, unique = true, updatable = false)
    private Long oid;
    private String surveyTitle;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @ManyToOne
    @JoinColumn(name = "classroom_oid")
    private Classroom classroom;
    private String courseTopic;
    @OneToMany
    private List<QuestionNumeric> questionNumeric;
    @OneToMany
    private List<QuestionText> questionText;
}
