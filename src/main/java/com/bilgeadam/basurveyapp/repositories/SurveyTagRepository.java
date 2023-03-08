package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.tags.SurveyTag;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyTagRepository extends BaseRepository<SurveyTag, Long> {
}
