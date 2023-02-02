package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "classrooms")
public class Classroom extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<User> users;
    @ManyToMany(mappedBy = "classrooms", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Survey> surveys;
}
