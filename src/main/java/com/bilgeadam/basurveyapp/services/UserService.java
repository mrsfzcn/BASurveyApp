package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.constant.ROLE_CONSTANTS;
import com.bilgeadam.basurveyapp.dto.request.AssignRoleToUserRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UserUpdateRequestDto;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.Role;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.RoleAlreadyExistException;
import com.bilgeadam.basurveyapp.mapper.UserMapper;
import com.bilgeadam.basurveyapp.repositories.QuestionRepository;
import com.bilgeadam.basurveyapp.repositories.QuestionTypeRepository;
import com.bilgeadam.basurveyapp.repositories.StudentRepository;
import com.bilgeadam.basurveyapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final QuestionTypeRepository questionTypeRepository;
    private final JwtService jwtService;
    private final RoleService roleService;
    private final StudentRepository studentRepository;


    public List<ManagerResponseDto> getManagerList() {
        List<User> managers = userRepository.findManagers();


        List<ManagerResponseDto> dto = UserMapper.INSTANCE.toManagerResponseDto(managers);
        return dto;

//        return students.stream().map(student -> UserResponseDto.builder()
//                .firstName(student.getFirstName())
//                .lastName(student.getLastName())
//                .email(student.getEmail())
//                .classrooms(student.getClassrooms().stream().map(Classroom::getName).collect(Collectors.toList()))
//                .build()).collect(Collectors.toList());
    }

    public List<AdminResponseDto> getAdminList() {
        List<User> admins = userRepository.findAdmins();

        List<AdminResponseDto> dto = UserMapper.INSTANCE.toAdminResponseDto(admins);
        return dto;

//        return students.stream().map(student -> UserResponseDto.builder()
//                .firstName(student.getFirstName())
//                .lastName(student.getLastName())
//                .email(student.getEmail())
//                .classrooms(student.getClassrooms().stream().map(Classroom::getName).collect(Collectors.toList()))
//                .build()).collect(Collectors.toList());
    }

    public Page<User> getUserPage(Pageable pageable) {
        // getCurrentUser()
        // user check
        return userRepository.findAllActive(pageable);
    }

    public User updateUser(Long userId, UserUpdateRequestDto dto) {
        // getCurrentUser()
        // user check
        Optional<User> userToBeUpdated = userRepository.findActiveById(userId);
        if (userToBeUpdated.isEmpty()) {
            throw new ResourceNotFoundException("User is not found");
        }
        userToBeUpdated.get().setFirstName(dto.getFirstName());
        userToBeUpdated.get().setLastName(dto.getLastName());
        return userRepository.save(userToBeUpdated.get());
    }

    public void deleteUser(Long userId) {
        // getCurrentUser()
        // user check
        Optional<User> userToBeDeleted = userRepository.findActiveById(userId);
        if (userToBeDeleted.isEmpty()) {
            throw new ResourceNotFoundException("User is not found");
        }
        userRepository.softDeleteById(userToBeDeleted.get().getOid(),"users");
    }

    public User findByOid(Long userId) {
        // getCurrentUser()
        // user check
        Optional<User> userById = userRepository.findActiveById(userId);
        if (userById.isEmpty()) {
            throw new ResourceNotFoundException("User is not found");
        }
        return userById.get();
    }

    public List<UserTrainersAndStudentsResponseDto> getTrainersAndStudentsList(String jwtToken) {
        Optional<User> user = userRepository.findByEmail(jwtService.extractEmail(jwtToken));
        if (user.isEmpty()) throw new ResourceNotFoundException("User is not found");
        if (!roleService.userHasRole(user.get(), ROLE_CONSTANTS.ROLE_MANAGER)) throw new AccessDeniedException("Unauthorized account");

        List<User> trainersWithStudent = userRepository.findTrainersAndStudents();

        List<UserTrainersAndStudentsResponseDto> dto = UserMapper.INSTANCE.toUserTrainersAndStudentsResponseDto(trainersWithStudent);
        return dto;

//        return
//        List<UserTrainersAndStudentsResponseDto> trainersAndStudentsList =
//                users.stream().map(u -> UserTrainersAndStudentsResponseDto.builder()
//                        .firstName(u.getFirstName())
//                        .lastName(u.getLastName())
//                        .email(u.getEmail())
//                        .roles(u.getRoles().stream().map(Role::getRole).collect(Collectors.toSet()))
//                        .build()).collect(Collectors.toList());
//        return Optional.of(trainersAndStudentsList);
    }


    public Boolean assignRoleToUser(AssignRoleToUserRequestDto request) {
//        if(!roleService.userHasRole(
//                userRepository.findByEmail(jwtService.extractEmail(request.getAuthorizedToken())).get(),
//                ROLE_CONSTANTS.ROLE_ADMIN))
//            throw new AccessDeniedException("Unauthorized account");
        Optional<User> user = userRepository.findByEmail(request.getUserEmail());
        if (user.isEmpty()) throw new ResourceNotFoundException("User is not found");
        if(!roleService.hasRole(request.getRole()))  throw new ResourceNotFoundException("Role is not found");
        if(roleService.userHasRole(user.get(), request.getRole())) throw new RoleAlreadyExistException("Role already exist");
        Role role = roleService.findActiveRole(request.getRole());
        user.get().getRoles().add(role);
        role.getUsers().add(user.get());
        userRepository.save(user.get());
        roleService.save(role);

        return true;
    }
}
