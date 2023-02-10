package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "responses")
public class Response extends BaseEntity {

    @Column(name = "response_string", nullable = false)
    private String responseString;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "oid", name = "question")
    private Question question;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_oid", nullable=false)
    private User user;

}
