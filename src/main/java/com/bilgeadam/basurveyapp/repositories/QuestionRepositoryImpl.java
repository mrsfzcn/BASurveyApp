package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.repositories.irepository.IQuestionRepository;
import com.bilgeadam.basurveyapp.repositories.utilities.RepositoryExtension;
import org.springframework.stereotype.Component;

@Component
public class QuestionRepositoryImpl extends RepositoryExtension<Question, Long> {
    private final IQuestionRepository questionRepository;

    public QuestionRepositoryImpl(IQuestionRepository questionRepository) {
        super(questionRepository);
        this.questionRepository = questionRepository;
    }
}
