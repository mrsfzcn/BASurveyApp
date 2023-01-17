package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.QuestionType;
import com.bilgeadam.basurveyapp.repositories.irepository.IQuestionTypeRepository;
import com.bilgeadam.basurveyapp.repositories.utilities.RepositoryExtension;
import org.springframework.stereotype.Component;

@Component
public class QuestionTypeRepositoryImpl extends RepositoryExtension<QuestionType, Long> {
    private final IQuestionTypeRepository questionTypeRepository;

    public QuestionTypeRepositoryImpl(IQuestionTypeRepository questionTypeRepository) {
        super(questionTypeRepository);
        this.questionTypeRepository = questionTypeRepository;
    }
}
