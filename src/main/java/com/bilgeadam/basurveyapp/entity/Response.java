package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.baseentity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "responses")
public class Response extends BaseEntity {
    @ManyToOne
    @JoinColumn(referencedColumnName = "oid", name = "question")
    private Question question;

    @ManyToOne
    @JoinColumn(name="user_oid", nullable=false)
    private User user;

    @Column(name = "response_string", nullable = false)
    private String responseString;
}
