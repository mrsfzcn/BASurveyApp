package com.bilgeadam.basurveyapp.entity;

import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class SurveyQuestionOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Survey survey;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    private Integer order;
}
