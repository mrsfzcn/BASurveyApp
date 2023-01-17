package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.baseentity.BaseEntity;
import com.bilgeadam.basurveyapp.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "users")
public class User extends BaseEntity {
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false, updatable = false, unique = true)
    private String email;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_classroom",
            joinColumns = @JoinColumn(name = "user_oid"),
            inverseJoinColumns = @JoinColumn(name = "classroom_oid"))
    private List<Classroom> classrooms;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_survey",
            joinColumns = @JoinColumn(name = "user_oid"),
            inverseJoinColumns = @JoinColumn(name = "survey_oid"))
    private List<Survey> surveys;
}
