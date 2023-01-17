package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.repositories.QuestionTypeRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionTypeService {
    private final QuestionTypeRepositoryImpl questionTypeRepository;

}
