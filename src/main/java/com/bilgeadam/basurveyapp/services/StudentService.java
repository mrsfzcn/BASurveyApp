package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.StudentUpdateDto;
import com.bilgeadam.basurveyapp.dto.response.StudentResponseDto;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
import com.bilgeadam.basurveyapp.mapper.StudentMapper;
import com.bilgeadam.basurveyapp.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentTagService studentTagService;
    public Boolean createStudent(Student student) {
        studentRepository.save(student);
        return true;
    }

    public StudentResponseDto updateStudent(StudentUpdateDto dto) {

        Optional<Student> student = studentRepository.findActiveById(dto.getStudentOid());
        Optional<StudentTag> studentTag = studentTagService.findActiveById(dto.getStudentTagOid());

        if (studentTag.isEmpty()) {
            throw new ResourceNotFoundException("Student tag is not found");
        }
        if (student.isEmpty()) {
            throw new ResourceNotFoundException("Student is not found");
        } else {
            student.get().getStudentTags().add(studentTag.get());
            studentTag.get().getTargetEntities().add(student.get());
            studentRepository.save(student.get());
            studentTagService.save(studentTag.get());
        }
       return StudentMapper.INSTANCE.toStudentResponseDto(student.get());
    }
    public Optional<Student> findByUser(User currentUser) {

        return studentRepository.findByUser(currentUser.getOid());
    }

    public List<Student> findByStudentTagOid(Long studentTagOid) {
        return studentRepository.findByStudentTagOid(studentTagOid);
    }

    public List<StudentResponseDto> getStudentList() {

        List<Student> students = studentRepository.findAllStudents();
        return StudentMapper.INSTANCE.toStudentResponseDtoList(students);
    }

    public void save(Student student) {
        studentRepository.save(student);
    }
}
