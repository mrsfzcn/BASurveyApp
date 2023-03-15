package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.response.StudentResponseDto;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.User;
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

    public Optional<Student> findByUser(User currentUser) {
        return studentRepository.findByUser(currentUser.getOid());
    }

    public List<Student> findByStudentTagOid(Long studentTagOid) {
        List<Student> studentList = studentTagService.findByStudentTagOid(studentTagOid);
        return studentList;
    }

    public List<StudentResponseDto> getStudentList() {

        List<Student> students = studentRepository.findAllStudents();

        List<StudentResponseDto> dto = StudentMapper.INSTANCE.toStudentResponseDtoList(students);
        return dto;
    }
}
