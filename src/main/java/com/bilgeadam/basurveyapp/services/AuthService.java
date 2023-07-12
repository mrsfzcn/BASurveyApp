package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.constant.ROLE_CONSTANTS;
import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.AuthenticationResponseDto;
import com.bilgeadam.basurveyapp.dto.response.RegisterResponseDto;
import com.bilgeadam.basurveyapp.entity.*;
import com.bilgeadam.basurveyapp.exceptions.ExceptionType;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.RoleNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.UserAlreadyExistsException;
import com.bilgeadam.basurveyapp.exceptions.custom.UserDoesNotExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Eralp Nitelik
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;
    private final ManagerService managerService;
    private final StudentService studentService;
    private final TrainerService trainerService;
    private final QrCodeService qrCodeService;

    @Transactional
    public RegisterResponseDto register(RegisterRequestDto request) {
        if (userService.findByEmail(request.getEmail()).isPresent()) {
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
                .twoFactory(false)
                .twoFactorKey(qrCodeService.generateSecret())
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
        userService.save(auth);

//        User auth = AuthMapper.INSTANCE.ToUser(request);
//        userService.save(auth);


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
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", auth.getAuthorizedRole());
        return RegisterResponseDto.builder()
                .token(jwtService.generateToken(claims,auth))
                .build();
    }

    public AuthenticationResponseDto authenticate(LoginRequestDto request) {
        Optional<User> user = userService.findByEmail(request.getEmail());
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
        userService.save(user.get());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.get().getAuthorizedRole());
        if (!user.get().isTwoFactory()) {
            user.get().setTwoFactory(true);
            userService.save(user.get());
            return AuthenticationResponseDto.builder()
                    .qrCode(qrCodeService.getUriForImage(user.get().getTwoFactorKey()))
                    .token(jwtService.generateToken(claims,user.get()))
                    .build();
        }

        return AuthenticationResponseDto.builder()
                .token(jwtService.generateToken(claims, user.get()))
                .build();
    }

    public AuthenticationResponseDto changeLogin(ChangeLoginRequestDto request) {
        Optional<User> authorizedUser = userService.findByEmail(jwtService.extractEmail(request.getAuthorizedToken()));
        if (authorizedUser.isEmpty()) throw new ResourceNotFoundException("User is not found");
//        if (!roleService.userHasAuthorizedRole(authorizedUser.get(), ROLE_CONSTANTS.ROLE_MANAGER))
//            throw new AccessDeniedException("Unauthorized account");
        Optional<User> user = userService.findByEmail(request.getUserEmail());
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
        userService.save(user.get());

        return AuthenticationResponseDto.builder()
                .token(jwtService.generateToken(user.get()))
                .build();
    }

    public AuthenticationResponseDto switchAuthorizationRoles(ChangeAuthorizedRequestDto request) {
        Optional<User> user = userService.findByEmail(jwtService.extractEmail(request.getAuthorizedToken()));
        if (user.isEmpty()) throw new ResourceNotFoundException("User is not found");

        if (roleService.userHasRole(user.get(), request.getAuthorizedRole()))
            user.get().setAuthorizedRole((request.getAuthorizedRole()));
        else throw new RoleNotFoundException("Role is not found");

        userService.save(user.get());

        return AuthenticationResponseDto.builder()
                .token(jwtService.generateToken(user.get()))
                .build();
    }

    public Boolean verifyCode(VerifyCodeRequestDto verifyCodeRequestDto) {


        String email = jwtService.extractEmail(verifyCodeRequestDto.getToken());
        System.out.println(verifyCodeRequestDto.getToken());
        Optional<User> user = userService.findByEmail(email);


        if (user.isEmpty()) throw new UserDoesNotExistsException(ExceptionType.USER_DOES_NOT_EXIST.getMessage());
        if (qrCodeService.verifyCode(verifyCodeRequestDto.getTwoFactoryKey(), user.get().getTwoFactorKey())) {
            return true;
        }
        return false;

    }
}
