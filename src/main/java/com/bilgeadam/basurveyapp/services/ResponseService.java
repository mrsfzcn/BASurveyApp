package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.repositories.ResponseRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResponseService {
    private final ResponseRepositoryImpl responseRepository;

}
