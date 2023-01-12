package com.bilgeadam.basurveyapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "questions_text")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionText {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "oid", nullable = false, unique = true, updatable = false)
    private Long oid;
    private String questionDescription;
    @ManyToOne
    private Survey survey;
    private Long order;


}
