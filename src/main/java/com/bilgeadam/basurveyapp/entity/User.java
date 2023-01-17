package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.baseentity.BaseEntity;
import com.bilgeadam.basurveyapp.entity.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

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
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "classroom_oid")
    private Classroom classroom;
}
