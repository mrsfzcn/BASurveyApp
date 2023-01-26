package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.repositories.irepository.ISurveyRepository;
import com.bilgeadam.basurveyapp.repositories.utilities.RepositoryExtension;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SurveyRepositoryImpl extends RepositoryExtension<Survey, Long> {
    private final ISurveyRepository surveyRepository;

    public SurveyRepositoryImpl(ISurveyRepository surveyRepository) {
        super(surveyRepository);
        this.surveyRepository = surveyRepository;
    }

    public Optional<Survey> findByOid(Long surveyId) {
        return surveyRepository.findByOid(surveyId);
    }
}
