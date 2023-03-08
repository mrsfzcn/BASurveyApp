package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public Boolean createStudent(Student student) {
        studentRepository.save(student);
        return true;
    }
}
