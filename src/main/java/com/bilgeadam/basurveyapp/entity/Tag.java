package com.bilgeadam.basurveyapp.entity;

import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "tag")
public class Tag extends BaseEntity {
    @Column(name = "tag_string")
    private String tagString;
    @ManyToMany(mappedBy = "tag", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Question> questions;

}
