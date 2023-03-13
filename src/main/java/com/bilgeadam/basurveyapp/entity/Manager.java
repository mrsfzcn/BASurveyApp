package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "managers")
public class Manager extends BaseEntity {
    @OneToOne
    User user;
}
