package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.repositories.ISurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final ISurveyRepository surveyRepository;

}