package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "students")
@JsonIgnoreProperties({"surveysAnswered", "studentTags"})
public class Student extends BaseEntity {
    @OneToOne
    User user;
    @ManyToMany(mappedBy = "studentsWhoAnswered",  fetch = FetchType.LAZY)
    Set<Survey> surveysAnswered;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy ="targetEntities"  ,fetch = FetchType.EAGER)
    Set<StudentTag> studentTags;

}
