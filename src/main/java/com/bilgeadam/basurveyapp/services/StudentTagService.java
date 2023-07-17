package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.response.StudentTagCreateResponseDto;
import com.bilgeadam.basurveyapp.dto.response.StudentTagDetailResponseDto;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTagNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.StudentTagExistException;
import com.bilgeadam.basurveyapp.exceptions.custom.StudentTagNotFoundException;
import com.bilgeadam.basurveyapp.mapper.TagMapper;
import com.bilgeadam.basurveyapp.repositories.StudentTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentTagService {
    private final StudentTagRepository studentTagRepository;

    public StudentTagCreateResponseDto createTag(CreateTagDto dto) {

        String tagString = dto.getTagString();

        if (studentTagRepository.findByTagName(tagString).isPresent()) {
            throw new StudentTagExistException("Student Tag already exists!");
        }
        StudentTag studentTag = StudentTag.builder()
                .tagString(tagString)
                .mainTagOid(dto.getMainTagOid())

                .build();

        studentTagRepository.save(studentTag);
        Optional<StudentTag> studentTagFinder = studentTagRepository.findByTagName(tagString);
        return StudentTagCreateResponseDto.builder()
                .studentTagId(studentTagFinder.get().getOid())
                .tagString(studentTagFinder.get().getTagString())
                .build();
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
            throw new StudentTagNotFoundException("Student Tag is not found");
        }
        return studentTagRepository.softDeleteById(deleteTag.get().getOid());
    }

    public List<StudentTagDetailResponseDto> getStudentTagList() {

        return TagMapper.INSTANCE.toStudentTagDetailResponseDtoList(studentTagRepository.findAllActive());
    }

    public void save(StudentTag studentTag) {

        studentTagRepository.save(studentTag);
    }

    public List<Long> findUserOidByStudentTagOid(Long studentTagOid) {

        return studentTagRepository.findUserOidByStudentTagOid(studentTagOid);
    }

    public Optional<StudentTag> findById(Long studentTagOid) {

        return studentTagRepository.findById(studentTagOid);
    }

    public List<Long> studentTagCount(Long studentTagOid){
        List<Long> count = studentTagRepository.findSurveyCountByStudentTag(studentTagOid);
        return count;
    }

    public void updateTagByTagString(String tagString, String newTagString) {
        StudentTag studentTag = studentTagRepository.findOptionalByTagString(tagString)
                .orElseThrow(() -> new QuestionTagNotFoundException("Question Tag not found"));
        studentTag.setTagString(newTagString);
       studentTagRepository.save(studentTag);
    }
}
