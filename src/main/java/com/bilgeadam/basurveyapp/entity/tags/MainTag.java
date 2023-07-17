package com.bilgeadam.basurveyapp.entity.tags;

import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import com.bilgeadam.basurveyapp.entity.base.BaseTag;
import com.bilgeadam.basurveyapp.entity.enums.Tags;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "maintag")
// tag ler öncelikle buraya kayıt olur buradan dağıtılır.
public class MainTag extends BaseEntity {
    String tagName;
    @Enumerated(EnumType.STRING)
    Tags tagClass;
}
