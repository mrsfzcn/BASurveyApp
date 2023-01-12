package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.repository.ISurveyRepository;
import com.bilgeadam.basurveyapp.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class SurveyService extends ServiceManager<Survey,Long> {
    private final ISurveyRepository surveyRepository;

    public SurveyService(ISurveyRepository surveyRepository) {
        super(surveyRepository);
        this.surveyRepository = surveyRepository;
    }
}