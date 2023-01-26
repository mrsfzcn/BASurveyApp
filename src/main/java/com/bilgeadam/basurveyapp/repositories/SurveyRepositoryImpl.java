package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.repositories.irepository.ISurveyRepository;
import com.bilgeadam.basurveyapp.repositories.utilities.RepositoryExtension;
import org.springframework.stereotype.Component;

@Component
public class SurveyRepositoryImpl extends RepositoryExtension<Survey, Long> {
    private final ISurveyRepository surveyRepository;

    public SurveyRepositoryImpl(ISurveyRepository surveyRepository) {
        super(surveyRepository);
        this.surveyRepository = surveyRepository;
    }


}
