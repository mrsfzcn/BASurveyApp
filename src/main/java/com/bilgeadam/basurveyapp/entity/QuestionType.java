package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "questiontypes")
@SQLDelete(sql = "UPDATE questiontypes SET state = 'DELETED' WHERE oid = ?1")
public class QuestionType extends BaseEntity {
    @Column(name = "question_type")
    private String questionType;
}
