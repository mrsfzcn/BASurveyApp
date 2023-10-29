package com.bilgeadam.basurveyapp.utilty;

import com.bilgeadam.basurveyapp.configuration.EmailService;
import com.bilgeadam.basurveyapp.dto.request.CreateQuestionTypeRequestDto;
import com.bilgeadam.basurveyapp.dto.request.CreateRoleDto;
import com.bilgeadam.basurveyapp.dto.request.RegisterRequestDto;
import com.bilgeadam.basurveyapp.entity.Role;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.services.AuthService;
import com.bilgeadam.basurveyapp.services.QuestionTypeService;
import com.bilgeadam.basurveyapp.services.RoleService;
import com.bilgeadam.basurveyapp.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Muhammed Furkan Türkmen
 */
@Component
@RequiredArgsConstructor
public class TestAndRun {
    private final UserService userService;
    private final AuthService authService;
    private final RoleService roleService;
    private final QuestionTypeService questionTypeService;
    private final EmailService emailService;
    @PostConstruct
    public void init() {
        new Thread(() -> {
            createBaseAdmin();
            createDefaultRoles();
            createDefaultQuestionTypes();
        }).start();

    }



    // uygulama ilk calisirken otomatik admin olusturur.
    public void createBaseAdmin() {
        Optional<User> admin=userService.findByEmail("admin.admin@bilgeadamboost.com");
        if (roleService.hasRole("ADMIN")) return;
        roleService.createRole(CreateRoleDto.builder().role("ADMIN").build());

        if (admin.isPresent()) return;
        authService.register(RegisterRequestDto.builder()
                .roles(Set.of("ADMIN"))
                .password("adminadmin")
                .firstName("admin")
                .lastName("admin")
                .build());

    }
    public void createDefaultRoles(){
        String[] roles = {"MANAGER", "MASTER_TRAINER", "ASSISTANT_TRAINER", "STUDENT"};
        for (String roleName : roles) {
            if (!roleService.hasRole(roleName)) {
                roleService.createRole(CreateRoleDto.builder().role(roleName).build());
            }
        }
    }

    private void createDefaultQuestionTypes() {
        if(!questionTypeService.existsByQuestionType("Likert")){
            questionTypeService.createQuestionType(CreateQuestionTypeRequestDto.builder().questionType("Çoktan Seçmeli").build());
            questionTypeService.createQuestionType(CreateQuestionTypeRequestDto.builder().questionType("Likert").build());
            questionTypeService.createQuestionType(CreateQuestionTypeRequestDto.builder().questionType("Çok Seçenekli Çok Seçilebilir").build());
            questionTypeService.createQuestionType(CreateQuestionTypeRequestDto.builder().questionType("Çok Seçenekli Çok Seçilebilir ve Seçenek Girilebilir").build());
            questionTypeService.createQuestionType(CreateQuestionTypeRequestDto.builder().questionType("Matriks").build());
            questionTypeService.createQuestionType(CreateQuestionTypeRequestDto.builder().questionType("Açık Uçlu").build());
        }
    }

}
