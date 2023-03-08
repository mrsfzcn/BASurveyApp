package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentTagRepository extends BaseRepository<StudentTag, Long> {
}
