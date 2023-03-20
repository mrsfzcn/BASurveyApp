package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateStudentTagRequestDto;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTypeNotFoundException;
import com.bilgeadam.basurveyapp.repositories.StudentTagRepository;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StudentTagService {
    private final StudentTagRepository studentTagRepository;
    private final BaseRepository studentRepository;

    public List<Student> getStudentsByStudentTag(StudentTag studentTag) {
        return studentTagRepository.findByStudentTagOid(studentTag.getOid());
    }

    public Optional<StudentTag> findByStudentTagName(String studentTag) {
        return studentTagRepository.findByTagName(studentTag);
    }

    public List<Student> findByStudentTagOid(Long studentTagOid) {
        return studentTagRepository.findByStudentTagOid(studentTagOid);
    }

    public Optional<StudentTag> findActiveById(Long studentTagOid) {
        return studentTagRepository.findActiveById(studentTagOid);
    }


    public Boolean createStudentTag(CreateStudentTagRequestDto dto) {

        StudentTag studentTag = StudentTag.builder()
                .tagString(dto.getTagString())
                .build();
        studentTagRepository.save(studentTag);
        return true;

    }
}
