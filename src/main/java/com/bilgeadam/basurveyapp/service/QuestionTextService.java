package com.bilgeadam.basurveyapp.service;

import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.repository.IQuestionTextRepository;
import com.bilgeadam.basurveyapp.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class QuestionTextService extends ServiceManager<Survey, Long> {

    private final IQuestionTextRepository questionTextRepository;

    public QuestionTextService(IQuestionTextRepository questionTextRepository) {
        super(questionTextRepository);
        this.questionTextRepository = questionTextRepository;
    }
}
