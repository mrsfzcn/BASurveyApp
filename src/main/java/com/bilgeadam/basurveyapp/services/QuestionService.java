package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.repositories.QuestionRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepositoryImpl questionRepository;

}
