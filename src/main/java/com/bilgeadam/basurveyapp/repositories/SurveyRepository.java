package com.bilgeadam.basurveyapp.repositories;


import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurveyRepository extends BaseRepository<Survey, Long> {


    Optional<Survey> findOptionalBySurveyTitle(String surveyTitle);
}