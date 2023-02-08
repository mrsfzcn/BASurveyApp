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
@Table(name = "classrooms")
public class Classroom extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<User> users;
    @ManyToMany(mappedBy = "classrooms", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Survey> surveys;
}
