package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.repositories.StudentTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentTagService {
    private final StudentTagRepository studentTagRepository;

    public void createTag(CreateTagDto dto) {
        StudentTag studentTag = StudentTag.builder()
                .tagString(dto.getTagString())
                .build();
        studentTagRepository.save(studentTag);
    }
    public List<Student> getStudentsByStudentTag(StudentTag studentTag) {
        return studentTagRepository.findByStudentTagOid(studentTag.getOid());
    }

    public Optional<StudentTag> findByStudentTag(StudentTag studentTag) {
        return studentTagRepository.findByTagName(studentTag.getTagString());
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
    public Boolean delete(Long studentTagOid) {
        Optional<StudentTag> deleteTag = studentTagRepository.findActiveById(studentTagOid);
        if (deleteTag.isEmpty()) {
            throw new RuntimeException("Tag is not found");
        } else {
            return studentTagRepository.softDeleteById(deleteTag.get().getOid());
        }
    }
}
