package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.StudentUpdateDto;
import com.bilgeadam.basurveyapp.dto.request.UserUpdateRequestDto;
import com.bilgeadam.basurveyapp.dto.response.StudentResponseDto;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
import com.bilgeadam.basurveyapp.mapper.StudentMapper;
import com.bilgeadam.basurveyapp.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
        if (student.isEmpty()) {
            throw new ResourceNotFoundException("Student is not found");
        } else {
            student.get().setStudentTags(studentTagService.findActiveById(dto.getStudentTagOid()).stream().collect(Collectors.toSet()));
            studentRepository.save(student.get());
        }
       StudentResponseDto studentResponseDto = StudentMapper.INSTANCE.toStudentResponseDto(student.get());
        return studentResponseDto;
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
