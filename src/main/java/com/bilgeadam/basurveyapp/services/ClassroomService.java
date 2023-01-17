package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.repositories.ClassroomRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassroomService {
    private final ClassroomRepositoryImpl classroomRepository;

}
