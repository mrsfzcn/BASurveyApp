package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import com.bilgeadam.basurveyapp.entity.base.BaseTag;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
@EqualsAndHashCode
@Table(name = "questiontag")
public class QuestionTag extends BaseTag<Question> {

}
