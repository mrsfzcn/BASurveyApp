package com.bilgeadam.basurveyapp.services.jobservices;

import com.bilgeadam.basurveyapp.dto.request.StudentModelResponse;
import com.bilgeadam.basurveyapp.dto.request.UserUpdateRequestDto;
import com.bilgeadam.basurveyapp.entity.*;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.services.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StudentJob {

    private final UserService userService;
    private final StudentService studentService;
    private final RoleService roleService;

    public StudentJob(UserService userService, StudentService studentService, RoleService roleService) {
        this.userService = userService;
        this.studentService = studentService;
        this.roleService = roleService;
    }


    public void checkStudentData(List<StudentModelResponse> students) {

        List<Role> roles = roleService.findRoles();
        List<User> savedStudents = userService.findByApiIdContainsAndState("student-", State.ACTIVE);
        List<User> deletedStudents = new ArrayList<>();
        savedStudents.forEach(savedStudent -> {
            Optional<StudentModelResponse> first = students.stream().filter(student -> savedStudent.getApiId().equals("student-" + student.getId())).findFirst();
            if (first.isEmpty())
                deletedStudents.add(savedStudent);
        });
        if (!deletedStudents.isEmpty())
            deletedStudents.forEach(user -> userService.deleteUser(user.getOid()));
        for (StudentModelResponse student : students) {
            Optional<User> user = userService.findByApiId("student-" + student.getId());
            if (user.isEmpty()) {
                User savedUser = userService.save(toUser(student, roles));
                Student apiStudent = new Student();
                apiStudent.setBaBoostEmail(student.getBaBoostEmail());
                apiStudent.setBaEmail(student.getBaEmail());
                apiStudent.setUser(savedUser);
                Branch apiBranch = new Branch();
                apiBranch.setOid(student.getBranchId());
                apiStudent.setBranch(apiBranch);
                CourseGroup apiGroup = new CourseGroup();
                apiGroup.setOid(student.getGroupId());
                apiStudent.setCourseGroup(apiGroup);
                studentService.createStudent(apiStudent);
            } else {
                if (!compareApiAndAppStudentData(student, user.get())) {
                    User updatedUser = userService.updateTrainerWithApiData(user.get().getEmail(), UserUpdateRequestDto.builder()
                            .firstName(student.getName())
                            .lastName(student.getSurname())
                            .email(student.getPersonalEmail())
                            .authorizedRole("STUDENT")
                            .build());
                    Optional<Student> optionalStudent = studentService.findByUser(updatedUser);
                    if (optionalStudent.isPresent()) {
                        optionalStudent.get().setBaEmail(student.getBaEmail());
                        optionalStudent.get().setBaBoostEmail(student.getBaBoostEmail());
                        optionalStudent.get().setUser(updatedUser);
                        Branch updatedBranch = new Branch();
                        updatedBranch.setOid(student.getBranchId());
                        optionalStudent.get().setBranch(updatedBranch);
                        CourseGroup updatedCourseGroup = new CourseGroup();
                        updatedCourseGroup.setOid(student.getGroupId());
                        optionalStudent.get().setCourseGroup(updatedCourseGroup);
                        optionalStudent.get().setState(State.ACTIVE);
                        studentService.createStudent(optionalStudent.get());
                    }
                }
            }
        }

    }

    User toUser(StudentModelResponse student, List<Role> roles) {
        Optional<Role> firstRole = roles.stream().filter(role -> role.getRole().equals("STUDENT")).findFirst();
        return User.builder()
                .apiId("student-" + student.getId())
                .firstName(student.getName())
                .lastName(student.getSurname())
                .password(null)
                .email(student.getPersonalEmail())
                .authorizedRole("STUDENT")
                .roles(firstRole.map(Set::of).orElse(null))
                .twoFactory(false)
                .twoFactorKey(null)
                .build();
    }

    public boolean compareApiAndAppStudentData(StudentModelResponse student, User user) {
        return student.getName().equals(user.getFirstName()) && student.getSurname().equals(user.getLastName()) && student.getPersonalEmail().equals(user.getEmail()) && user.getAuthorizedRole().equals("STUDENT");
    }
}
