package com.bilgeadam.basurveyapp.entity.base;

import com.bilgeadam.basurveyapp.entity.Question;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;


@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode
@RequiredArgsConstructor
@MappedSuperclass
public abstract class BaseTag<T> extends BaseEntity{
    @Column(name = "tag_string")
     String tagString;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<T> targetEntities;
}
