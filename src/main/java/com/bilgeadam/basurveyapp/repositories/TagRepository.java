package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Tag;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends BaseRepository<Tag, Long> {
}
