package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "questions")
public class Question extends BaseEntity {
    @Column(name = "question_string")
    private String questionString;
    private String role;
    @Column(name = "question_order")
    private Integer order;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "oid", name = "question_type")
    private QuestionType questionType;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Response> responses;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "oid", name = "survey")
    private Survey survey;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Tag tag;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable( name = "users_role", joinColumns =
    @JoinColumn(name = "questions_oid", referencedColumnName = "oid"), inverseJoinColumns = @JoinColumn(name = "users_oid", referencedColumnName = "oid"))
    private List<User> users;

}
