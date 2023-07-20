package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.constant.ROLE_CONSTANTS;
import com.bilgeadam.basurveyapp.dto.request.AssignRoleToUserRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UserUpdateRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AdminResponseDto;
import com.bilgeadam.basurveyapp.dto.response.ManagerResponseDto;
import com.bilgeadam.basurveyapp.dto.response.UserSimpleResponseDto;
import com.bilgeadam.basurveyapp.dto.response.UserTrainersAndStudentsResponseDto;
import com.bilgeadam.basurveyapp.entity.Role;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.exceptions.custom.RoleAlreadyExistException;
import com.bilgeadam.basurveyapp.exceptions.custom.RoleNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.UserDoesNotExistsException;
import com.bilgeadam.basurveyapp.mapper.UserMapper;
import com.bilgeadam.basurveyapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, @Lazy JwtService jwtService, @Lazy RoleService roleService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.roleService = roleService;
    }

    public List<ManagerResponseDto> getManagerList() {
        List<User> managers = userRepository.findManagers();

        return UserMapper.INSTANCE.toManagerResponseDto(managers);
    }

    public List<AdminResponseDto> getAdminList() {

        List<User> admins = userRepository.findAdmins();
        return UserMapper.INSTANCE.toAdminResponseDto(admins);
    }

    public Page<User> getUserPage(Pageable pageable) {

        return userRepository.findAllActive(pageable);
    }

    public User updateUser(String userEmail, UserUpdateRequestDto dto) {

        Optional<User> userToBeUpdated = userRepository.findByEmail(userEmail);
        if (userToBeUpdated.isEmpty()) {
            throw new UserDoesNotExistsException("User is not found");
        }
        userToBeUpdated.get().setFirstName(dto.getFirstName());
        userToBeUpdated.get().setLastName(dto.getLastName());
        userToBeUpdated.get().setEmail(dto.getEmail());
        userToBeUpdated.get().setAuthorizedRole(dto.getAuthorizedRole());
        return userRepository.save(userToBeUpdated.get());
    }

    public boolean deleteUser(Long userId) {

        Optional<User> userToBeDeleted = userRepository.findActiveById(userId);
        if (userToBeDeleted.isEmpty()) {
            throw new UserDoesNotExistsException("User is not found");
        }
        return userRepository.softDeleteById(userToBeDeleted.get().getOid());
    }

    public UserSimpleResponseDto findByOid(Long userId) {

        Optional<User> userById = userRepository.findActiveById(userId);
        if (userById.isEmpty()) {
            throw new UserDoesNotExistsException("User is not found");
        }
        return UserMapper.INSTANCE.toUserSimpleResponseDto(userById.get());
    }

    public List<UserTrainersAndStudentsResponseDto> getTrainersAndStudentsList(String jwtToken) {
        Optional<User> user = userRepository.findByEmail(jwtService.extractEmail(jwtToken));
        if (user.isEmpty()) throw new UserDoesNotExistsException("User is not found");
        if (!roleService.userHasRole(user.get(), ROLE_CONSTANTS.ROLE_MANAGER)) throw new AccessDeniedException("Unauthorized account");

        List<User> trainersWithStudent = userRepository.findTrainersAndStudents();

        return UserMapper.INSTANCE.toUserTrainersAndStudentsResponseDto(trainersWithStudent);
    }


    public Boolean assignRoleToUser(AssignRoleToUserRequestDto request) {

        Optional<User> user = userRepository.findByEmail(request.getUserEmail());
        if (user.isEmpty()) throw new UserDoesNotExistsException("User is not found");
        if(!roleService.hasRole(request.getRole()))  throw new RoleNotFoundException("Role is not found");
        if(roleService.userHasRole(user.get(), request.getRole())) throw new RoleAlreadyExistException("Role already exist");
        Role role = roleService.findActiveRole(request.getRole());
        user.get().getRoles().add(role);
        role.getUsers().add(user.get());
        userRepository.save(user.get());
        roleService.save(role);

        return true;
    }

    public Optional<User> findActiveById(Long userOid) {

        return userRepository.findActiveById(userOid);
    }

    public Optional<User> findByEmail(String extractEmail) {

        return userRepository.findByEmail(extractEmail);
    }


    public void save(User auth) {
        userRepository.save(auth);
    }

    public Optional<User> findById(Long id) {
        return findById(id);
    }
}
