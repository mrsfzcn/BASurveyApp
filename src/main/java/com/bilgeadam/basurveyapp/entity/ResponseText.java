package com.bilgeadam.basurveyapp.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "response_texts")
public class ResponseText {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "oid", nullable = false)
    private Long oid;
    @Column(unique = true, nullable = false)
    private String questionText;
    @ManyToOne
    @JoinColumn(name="oid", nullable=false)
    private User user;
    @Column(unique = true, nullable = false)
    private String textResponse;
}
