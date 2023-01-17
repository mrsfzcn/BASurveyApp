package com.bilgeadam.basurveyapp.entity.baseentity;

import com.bilgeadam.basurveyapp.entity.enums.State;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oid;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "created_by_user_oid")
    private Long createdBy;

    @Column(name = "updated_by_user_oid")
    private Long updatedBy;

    @Enumerated(EnumType.STRING)
    private State state;
}
