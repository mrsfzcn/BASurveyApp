package com.bilgeadam.basurveyapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "questions_text")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionText {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "oid", nullable = false, unique = true, updatable = false)
    private Long oid;
    private String questionDescription;
    private Survey survey;
    private Long order;


}
