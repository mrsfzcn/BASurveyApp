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
    private String classroom;
    private String courseTopic;
    private List<QuestionNumeric> numericQuestions;
    private List<ResponseNumeric> numericResponses;
    private List<QuestionText> textQuestions;
    private List<ResponseText> textResponses;
}
