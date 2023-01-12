package com.bilgeadam.basurveyapp.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "responses_numeric")
public class ResponseNumeric {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long oid;
    @ManyToOne
    @JoinColumn(name = "question_numeric")
    private QuestionNumeric questionNumeric;
    @ManyToOne
    private User user;
    @Column(name = "numeric_response")
    private int numericResponse;
}
