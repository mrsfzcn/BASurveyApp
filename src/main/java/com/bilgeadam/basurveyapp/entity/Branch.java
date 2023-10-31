package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name","location"}))
public class Branch extends BaseEntity{


    private String name;
    private String location;


    @OneToMany(mappedBy = "branch")
    private List<Student> students;

    @OneToMany(mappedBy = "branch")
    private List<Trainer> trainers;

    @OneToMany(mappedBy = "branch")
    private List<Manager> managers;
}
