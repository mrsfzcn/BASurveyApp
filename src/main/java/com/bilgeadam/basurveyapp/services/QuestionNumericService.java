package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.QuestionNumeric;
import com.bilgeadam.basurveyapp.repository.IQuestionNumericRepository;
import com.bilgeadam.basurveyapp.utility.ServiceManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionNumericService extends ServiceManager<QuestionNumeric,Long> {

    private final IQuestionNumericRepository iQuestionNumericRepository;

    public QuestionNumericService(IQuestionNumericRepository iQuestionNumericRepository) {
        super(iQuestionNumericRepository);
        this.iQuestionNumericRepository = iQuestionNumericRepository;
    }
}
