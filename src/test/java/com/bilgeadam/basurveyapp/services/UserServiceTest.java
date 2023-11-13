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
import com.bilgeadam.basurveyapp.exceptions.custom.UserDoesNotExistsException;
import com.bilgeadam.basurveyapp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Mert Cömertoğlu
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private RoleService roleService;

    private StudentService studentService;
    private UserService userService;

    private TrainerService trainerService;
    private ManagerService managerService;

    public UserServiceTest() {
    }

    @BeforeAll
    public void Init(){
        MockitoAnnotations.openMocks(this);
        userService= new UserService(userRepository,jwtService,roleService, studentService, trainerService, managerService);
    }

    @Test
    public void testGetManagerList() {
        List<User> managers = Arrays.asList(new User(), new User());
        when(userRepository.findManagers()).thenReturn(managers);

        List<ManagerResponseDto> result = userService.getManagerList();

        assertEquals(managers.size(), result.size());
    }

    @Test
    public void testGetAdminList() {
        List<User> admins = Arrays.asList(new User(), new User());
        when(userRepository.findAdmins()).thenReturn(admins);

        List<AdminResponseDto> result = userService.getAdminList();

        assertEquals(admins.size(), result.size());
    }

    @Test
    public void testGetUserPage() {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = Arrays.asList(new User(), new User());
        Page<User> expected = new PageImpl<>(users, pageable, users.size());
        when(userRepository.findAllActive(pageable)).thenReturn(expected);

        Page<User> result = userService.getUserPage(pageable);

        assertEquals(expected, result);
    }

    @Test
    public void testUpdateUser() {
        String email = "test@bilgeadam.com";

        UserUpdateRequestDto dto = new UserUpdateRequestDto();
        dto.setFirstName("Ad");
        dto.setLastName("Soyad");

        User userToBeUpdated = new User();
        userToBeUpdated.setOid(1L);
        userToBeUpdated.setEmail("test@bilgeadam.com");
        userToBeUpdated.setFirstName("Ad");
        userToBeUpdated.setLastName("Soyad");

        when(userRepository.findActiveUserByEmail(email)).thenReturn(Optional.of(userToBeUpdated));
        when(userRepository.save(userToBeUpdated)).thenReturn(userToBeUpdated);

        User result = userService.updateUser(email, dto);

        assertEquals(dto.getFirstName(), result.getFirstName());
        assertEquals(dto.getLastName(), result.getLastName());
    }

    @Test
    public void testUpdateUserWithNonExistingUser()  {
        String email = "test@bilgeadam.com";
        UserUpdateRequestDto dto = new UserUpdateRequestDto();
        when(userRepository.findActiveUserByEmail(email)).thenReturn(Optional.empty());

        try {
            userService.updateUser(email, dto);
            fail("Expected ResourceNotFoundException was not thrown");
        } catch (UserDoesNotExistsException e) {
            assertEquals("User is not found", e.getMessage());
        }
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;
        User userToBeDeleted = new User();
        userToBeDeleted.setOid(userId);
        userToBeDeleted.setEmail("test@example.com");
        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(userToBeDeleted));
        when(userRepository.softDeleteById(userId)).thenReturn(true);

        boolean result = userService.deleteUser(userId);

        assertTrue(result);
    }

    @Test
    public void testFindByOid() {
        Long userId = 1L;
        User user = new User();
        user.setFirstName("Test");
        user.setEmail("test@bilgeadam.com");
        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(user));

        UserSimpleResponseDto result = userService.findByOid(userId);

        assertEquals("Test", result.getFirstName());
        assertEquals("test@bilgeadam.com", result.getEmail());
    }

    @Test
    public void testFindByOid_UserDoesNotExistsException() {
        Long userId = 1L;
        Optional<User> mockUser = Optional.empty();
        Mockito.when(userRepository.findActiveById(userId)).thenReturn(mockUser);

        try {
            userService.findByOid(userId);
            fail("Expected UserDoesNotExistsException was not thrown");
        } catch (UserDoesNotExistsException e) {
            assertEquals("User is not found", e.getMessage());
        }
    }

    @Test
    public void testDeleteUserWithNonExistingUser() {
        Long userId = 1L;
        when(userRepository.findActiveById(userId)).thenReturn(Optional.empty());

        try {
            userService.deleteUser(userId);
            fail("Expected UserDoesNotExistsException was not thrown");
        } catch (UserDoesNotExistsException e) {
        assertEquals("User is not found", e.getMessage());
        }

    }

    @Test
    public void testGetTrainersAndStudentsList() {
        String jwtToken = "sampleToken";
        User user = User.builder()
                .email("test@bilgeadam.com")
                .authorizedRole(ROLE_CONSTANTS.ROLE_MANAGER)
                .build();
        when(jwtService.extractEmail(jwtToken)).thenReturn(user.getEmail());
        when(userRepository.findActiveUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(roleService.userHasRole(user, ROLE_CONSTANTS.ROLE_MANAGER)).thenReturn(true);

        User trainer = User.builder()
                .firstName("Trainer")
                .lastName("Test")
                .email("trainer@bilgeadam.com")
                .authorizedRole(ROLE_CONSTANTS.ROLE_MASTER_TRAINER)
                .build();
        User student = User.builder()
                .firstName("Student")
                .lastName("Test")
                .email("student@bilgeadam.com")
                .authorizedRole(ROLE_CONSTANTS.ROLE_STUDENT)
                .build();
        List<User> trainersWithStudent = List.of(trainer, student);
        when(userRepository.findTrainersAndStudents()).thenReturn(trainersWithStudent);

        List<UserTrainersAndStudentsResponseDto> expectedDto = List.of(
                UserTrainersAndStudentsResponseDto.builder()
                        .firstName(trainer.getFirstName())
                        .lastName(trainer.getLastName())
                        .email(trainer.getEmail())
                        .build(),
                UserTrainersAndStudentsResponseDto.builder()
                        .firstName(student.getFirstName())
                        .lastName(student.getLastName())
                        .email(student.getEmail())
                        .build()
        );

        List<UserTrainersAndStudentsResponseDto> actualDto = userService.getTrainersAndStudentsList(jwtToken);

        assertTrue(actualDto.toString().equals(expectedDto.toString()));
    }

    @Test
    public void testGetTrainersAndStudentsListWhenUserNotFound() {
        String jwtToken = "sampleToken";
        when(userRepository.findActiveUserByEmail(jwtService.extractEmail(jwtToken))).thenReturn(Optional.empty());

        try {
            userService.getTrainersAndStudentsList(jwtToken);
            fail("Expected UserDoesNotExistsException was not thrown");
        } catch (UserDoesNotExistsException e) {
            assertEquals("User is not found", e.getMessage());
        }
    }

    @Test
    public void testAssignRoleToUser() {

        AssignRoleToUserRequestDto request = AssignRoleToUserRequestDto.builder()
                .userEmail("test@bilgeadam.com")
                .role("ROLE_ADMIN")
                .build();

        User user = User.builder()
                .email(request.getUserEmail())
                .roles(new HashSet<>())
                .build();

        Role role = Role.builder()
                .role(request.getRole())
                .users(new ArrayList<>())
                .build();

        when(userRepository.findActiveUserByEmail(request.getUserEmail())).thenReturn(Optional.of(user));
        when(roleService.hasRole(request.getRole())).thenReturn(true);
        when(roleService.userHasRole(user, request.getRole())).thenReturn(false);
        when(roleService.findActiveRole(request.getRole())).thenReturn(role);

        Boolean result = userService.assignRoleToUser(request);

        assertTrue(result);
        assertTrue(user.getRoles().contains(role));
        assertTrue(role.getUsers().contains(user));
        verify(userRepository).save(user);
        verify(roleService).save(role);
    }
}
