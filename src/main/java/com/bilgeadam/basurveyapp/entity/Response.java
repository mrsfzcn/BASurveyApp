package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "response_string", nullable = false)
    private String responseString; //todo: çok seçmeli sorularda çoklu cevap kaydedilebiliyor.Burası list tutulabilir
    @JsonManagedReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "oid", name = "question")
    private Question question;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_oid", nullable=false)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    private Survey survey;

}
