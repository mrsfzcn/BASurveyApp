package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends BaseRepository<Survey, Long> {
}