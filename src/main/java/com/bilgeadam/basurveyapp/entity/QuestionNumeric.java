package com.bilgeadam.basurveyapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "questions_numeric")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionNumeric {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "oid", nullable = false, unique = true, updatable = false)
    private Long oid;
    @Column(name = "question_description")
    private String questionDescription;
    @ManyToOne
    private Survey survey;
    private Long order;
}
