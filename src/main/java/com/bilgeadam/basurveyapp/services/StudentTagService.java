package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.repositories.StudentTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentTagService {
    private final StudentTagRepository studentTagRepository;
    public List<Student> getStudentsByStudentTag(StudentTag studentTag) {
        return studentTagRepository.getStudentsByStudentTag(studentTag.getOid());
    }
}
