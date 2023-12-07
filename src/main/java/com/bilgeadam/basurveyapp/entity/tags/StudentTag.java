package com.bilgeadam.basurveyapp.entity.tags;

import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.base.BaseTag;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@ToString
@Setter
@Entity
@SuperBuilder
@Table(name = "studenttags")
public class StudentTag extends BaseTag<Student> {

}
