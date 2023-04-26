package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.constant.ROLE_CONSTANTS;
import com.bilgeadam.basurveyapp.dto.request.ChangeAuthorizedRequestDto;
import com.bilgeadam.basurveyapp.dto.request.ChangeLoginRequestDto;
import com.bilgeadam.basurveyapp.dto.request.LoginRequestDto;
import com.bilgeadam.basurveyapp.dto.request.RegisterRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AuthenticationResponseDto;
import com.bilgeadam.basurveyapp.entity.*;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.RoleNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.UserAlreadyExistsException;
import com.bilgeadam.basurveyapp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Eralp Nitelik
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;

    private final ManagerService managerService;
    private final StudentService studentService;
    private final TrainerService trainerService;

    @Transactional
    public AuthenticationResponseDto register(RegisterRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already registered.");
        }
        List<Role> roles = roleService.findRoles();
        Set<Role> userRoles = roles.stream().filter(role -> request.getRoles().contains(role.getRole())).collect(Collectors.toSet());
        if (userRoles.isEmpty()) {
            throw new RoleNotFoundException("Given roles not found.");
        }


        User auth = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .roles(userRoles)
                .build();
        if (roleService.userHasRole(auth, ROLE_CONSTANTS.ROLE_ADMIN))
            auth.setAuthorizedRole((ROLE_CONSTANTS.ROLE_ADMIN));
        else if (roleService.userHasRole(auth, ROLE_CONSTANTS.ROLE_MANAGER))
            auth.setAuthorizedRole((ROLE_CONSTANTS.ROLE_MANAGER));
        else if (roleService.userHasRole(auth, ROLE_CONSTANTS.ROLE_MASTER_TRAINER))
            auth.setAuthorizedRole((ROLE_CONSTANTS.ROLE_MASTER_TRAINER));
        else if (roleService.userHasRole(auth, ROLE_CONSTANTS.ROLE_ASSISTANT_TRAINER))
            auth.setAuthorizedRole((ROLE_CONSTANTS.ROLE_ASSISTANT_TRAINER));
        else if (roleService.userHasRole(auth, ROLE_CONSTANTS.ROLE_STUDENT))
            auth.setAuthorizedRole((ROLE_CONSTANTS.ROLE_STUDENT));
        else throw new RoleNotFoundException("Role is not found");
        userRepository.save(auth);

//        User auth = AuthMapper.INSTANCE.ToUser(request);
//        userRepository.save(auth);


        if (roleService.userHasRole(auth, ROLE_CONSTANTS.ROLE_ADMIN)
                || roleService.userHasRole(auth, ROLE_CONSTANTS.ROLE_MANAGER)) {
            Manager manager = new Manager();
            manager.setUser(auth);
            managerService.createManager(manager);
        }
        if (roleService.userHasRole(auth, ROLE_CONSTANTS.ROLE_STUDENT)) {
            Student student = new Student();
            student.setUser(auth);
            studentService.createStudent(student);
        }
        if (roleService.userHasRole(auth, ROLE_CONSTANTS.ROLE_MASTER_TRAINER)
                || roleService.userHasRole(auth, ROLE_CONSTANTS.ROLE_ASSISTANT_TRAINER)) {
            Trainer trainer = new Trainer();
            trainer.setMasterTrainer(roleService.userHasRole(auth, ROLE_CONSTANTS.ROLE_MASTER_TRAINER));
            trainer.setUser(auth);
            trainerService.createTrainer(trainer);
        }
        return AuthenticationResponseDto.builder()
                .token(jwtService.generateToken(auth))
                .build();
    }

    public AuthenticationResponseDto authenticate(LoginRequestDto request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Username does not exist.");
        }
        if (roleService.userHasRole(user.get(), ROLE_CONSTANTS.ROLE_ADMIN))
            user.get().setAuthorizedRole((ROLE_CONSTANTS.ROLE_ADMIN));
        else if (roleService.userHasRole(user.get(), ROLE_CONSTANTS.ROLE_MANAGER))
            user.get().setAuthorizedRole((ROLE_CONSTANTS.ROLE_MANAGER));
        else if (roleService.userHasRole(user.get(), ROLE_CONSTANTS.ROLE_MASTER_TRAINER))
            user.get().setAuthorizedRole((ROLE_CONSTANTS.ROLE_MASTER_TRAINER));
        else if (roleService.userHasRole(user.get(), ROLE_CONSTANTS.ROLE_ASSISTANT_TRAINER))
            user.get().setAuthorizedRole((ROLE_CONSTANTS.ROLE_ASSISTANT_TRAINER));
        else if (roleService.userHasRole(user.get(), ROLE_CONSTANTS.ROLE_STUDENT))
            user.get().setAuthorizedRole((ROLE_CONSTANTS.ROLE_STUDENT));
        else throw new RoleNotFoundException("Role is not found");
        userRepository.save(user.get());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        return AuthenticationResponseDto.builder()
                .token(jwtService.generateToken(user.get()))
                .build();
    }

    public AuthenticationResponseDto changeLogin(ChangeLoginRequestDto request) {
        Optional<User> authorizedUser = userRepository.findByEmail(jwtService.extractEmail(request.getAuthorizedToken()));
        if (authorizedUser.isEmpty()) throw new ResourceNotFoundException("User is not found");
//        if (!roleService.userHasAuthorizedRole(authorizedUser.get(), ROLE_CONSTANTS.ROLE_MANAGER))
//            throw new AccessDeniedException("Unauthorized account");
        Optional<User> user = userRepository.findByEmail(request.getUserEmail());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Username does not exist.");
        }

        if (roleService.userHasRole(user.get(), ROLE_CONSTANTS.ROLE_MANAGER) || roleService.userHasRole(user.get(), ROLE_CONSTANTS.ROLE_ADMIN)) {
            throw new AccessDeniedException("You can't change login to other admin or manager account");
        }

        if (roleService.userHasRole(user.get(), ROLE_CONSTANTS.ROLE_MASTER_TRAINER))
            user.get().setAuthorizedRole((ROLE_CONSTANTS.ROLE_MASTER_TRAINER));
        else if (roleService.userHasRole(user.get(), ROLE_CONSTANTS.ROLE_ASSISTANT_TRAINER))
            user.get().setAuthorizedRole((ROLE_CONSTANTS.ROLE_ASSISTANT_TRAINER));
        else if (roleService.userHasRole(user.get(), ROLE_CONSTANTS.ROLE_STUDENT))
            user.get().setAuthorizedRole((ROLE_CONSTANTS.ROLE_STUDENT));
        else throw new RoleNotFoundException("Role is not found");
        userRepository.save(user.get());

        return AuthenticationResponseDto.builder()
                .token(jwtService.generateToken(user.get()))
                .build();
    }

    public AuthenticationResponseDto changeAuthorized(ChangeAuthorizedRequestDto request) {
        Optional<User> user = userRepository.findByEmail(jwtService.extractEmail(request.getAuthorizedToken()));
        if (user.isEmpty()) throw new ResourceNotFoundException("User is not found");

        if (roleService.userHasRole(user.get(), request.getAuthorizedRole()))
            user.get().setAuthorizedRole((request.getAuthorizedRole()));
        else throw new RoleNotFoundException("Role is not found");

        userRepository.save(user.get());

        return AuthenticationResponseDto.builder()
                .token(jwtService.generateToken(user.get()))
                .build();
    }
}
