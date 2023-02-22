package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.dto.request.ChangeLoginRequestDto;
import com.bilgeadam.basurveyapp.dto.request.LoginRequestDto;
import com.bilgeadam.basurveyapp.dto.request.RegisterRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AuthenticationResponseDto;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.entity.enums.Role;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
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

import java.util.Optional;

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

    @Transactional
    public AuthenticationResponseDto register(RegisterRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already registered.");
        }
        User auth = userRepository.save(User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole())
                .build());
        return AuthenticationResponseDto.builder()
                .token(jwtService.generateToken(auth))
                .build();
    }

    public AuthenticationResponseDto authenticate(LoginRequestDto request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Username does not exist.");
        }
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

    public AuthenticationResponseDto changeAuthenticate(ChangeLoginRequestDto request) {
        Optional<User> authorizedUser = userRepository.findByEmail(jwtService.extractEmail(request.getAuthorizedToken()));
        if(authorizedUser.isEmpty()) throw new ResourceNotFoundException("User is not found");
        if(!authorizedUser.get().getRole().equals(Role.MANAGER)) throw new AccessDeniedException("Unauthorized account");
        Optional<User> user = userRepository.findByEmail(request.getUserEmail());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Username does not exist.");
        }

        if(user.get().getRole().equals(Role.MANAGER) || user.get().getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Unauthorized account");
        }

        return AuthenticationResponseDto.builder()
                .token(jwtService.generateToken(user.get()))
                .build();
    }
}
