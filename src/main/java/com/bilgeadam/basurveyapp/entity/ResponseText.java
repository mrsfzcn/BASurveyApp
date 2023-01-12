package com.bilgeadam.basurveyapp.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "response_texts")
public class ResponseText {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "oid", nullable = false)
    private Long oid;
    @ManyToOne
    @JoinColumn(referencedColumnName = "oid", name = "question_text")
    private QuestionText questionText;
    @ManyToOne
    @JoinColumn(name="oid", nullable=false)
    private User user;
    @Column(name = "text_response", unique = true, nullable = false)
    private String textResponse;
}
