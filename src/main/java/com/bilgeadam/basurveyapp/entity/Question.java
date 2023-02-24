package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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
    // TODO better solution
    private String role;
    @Column(name = "question_order")
    private Integer order;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "oid", name = "question_type")
    private QuestionType questionType;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Response> responses;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@JoinColumn(referencedColumnName = "oid", name = "survey")
    private List<Survey> surveys;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tag> tag;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubTag> subtag;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable( name = "users_role", joinColumns =
    @JoinColumn(name = "questions_oid", referencedColumnName = "oid"), inverseJoinColumns = @JoinColumn(name = "users_oid", referencedColumnName = "oid"))
    private List<User> users;


}
