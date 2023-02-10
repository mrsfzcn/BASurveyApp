package com.bilgeadam.basurveyapp.entity.base;

import com.bilgeadam.basurveyapp.entity.enums.State;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * @author Eralp Nitelik
 */
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@RequiredArgsConstructor
@EqualsAndHashCode
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue
    private Long oid;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;


    // Saves as 0 if auditor is anonymous.
    @CreatedBy
    @Column(name = "created_by")
    private Long createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;

    @Enumerated(EnumType.STRING)
    @Nonnull
    private State state;

    @PrePersist
    public void prePersist() {
        setCreatedAt(new Date());
        setUpdatedAt(new Date());
        setState(State.ACTIVE);
    }

    @PreUpdate
    public void preUpdate() {
        setUpdatedAt(new Date());
    }
}
