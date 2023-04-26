package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.constant.ROLE_CONSTANTS;
import com.bilgeadam.basurveyapp.dto.request.CreateRoleDto;
import com.bilgeadam.basurveyapp.dto.response.CreateRoleResponseDto;
import com.bilgeadam.basurveyapp.entity.Role;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.repositories.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Mert Cömertoğlu
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;

    private RoleService roleService;

    @BeforeAll
    public void Init() {

        MockitoAnnotations.openMocks(this);
        roleService = new RoleService(roleRepository);
    }

    @Test
    public void testUserHasRole(){
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder()
                .role(ROLE_CONSTANTS.ROLE_MANAGER)
                .build());
        User user = User.builder()
                .email("test@bilgeadam.com")
                .roles(roles)
                .authorizedRole(ROLE_CONSTANTS.ROLE_MANAGER)
                .build();

        // Test user has the specified role
        assertTrue(roleService.userHasRole(user, ROLE_CONSTANTS.ROLE_MANAGER));
    }

    @Test
    public void testUserHasAuthorizedRole(){
        User user = User.builder()
                .email("test@bilgeadam.com")
                .authorizedRole(ROLE_CONSTANTS.ROLE_MANAGER)
                .build();

        // Test user has the specified role
        assertTrue(roleService.userHasAuthorizedRole(user, ROLE_CONSTANTS.ROLE_MANAGER));
    }

    @Test
    public void testFindRoleStrings(){
        Role role1 = new Role();
        role1.setRole("ROLE_ADMIN");
        Role role2 = new Role();
        role2.setRole("ROLE_USER");

        when(roleRepository.findAllActive()).thenReturn(Arrays.asList(role1, role2));

        // when
        List<String> roles = roleService.findRoleStrings();

        // then
        Assertions.assertEquals(2, roles.size());
        Assertions.assertTrue(roles.contains("ROLE_ADMIN"));
        Assertions.assertTrue(roles.contains("ROLE_USER"));
    }

    @Test
    public void testHasRole(){
        Role role1 = new Role();
        role1.setRole(ROLE_CONSTANTS.ROLE_ADMIN);
        Role role2 = new Role();
        role2.setRole(ROLE_CONSTANTS.ROLE_STUDENT);

        when(roleRepository.findAllActive()).thenReturn(Arrays.asList(role1, role2));

        assertTrue(roleService.hasRole(ROLE_CONSTANTS.ROLE_ADMIN));
        assertTrue(roleService.hasRole(ROLE_CONSTANTS.ROLE_STUDENT));
        assertFalse(roleService.hasRole(ROLE_CONSTANTS.ROLE_MANAGER));
    }

    @Test
    public void testFindRoles(){
        Role role1 = new Role();
        role1.setRole(ROLE_CONSTANTS.ROLE_ADMIN);
        Role role2 = new Role();
        role2.setRole(ROLE_CONSTANTS.ROLE_STUDENT);

        when(roleRepository.findAllActive()).thenReturn(Arrays.asList(role1, role2));

        List<Role> result = roleService.findRoles();
        assertEquals(2, result.size());
        assertEquals(ROLE_CONSTANTS.ROLE_ADMIN,result.get(0).getRole());
    }

    @Test
    public void testCreateRole(){

        CreateRoleDto createRoleDto = CreateRoleDto.builder()
                .role(ROLE_CONSTANTS.ROLE_ADMIN)
                .build();

        Role role = Role.builder()
                .role(createRoleDto.getRole().toUpperCase())
                .build();

        when(roleRepository.save(role)).thenReturn(role);

        CreateRoleResponseDto result = roleService.createRole(createRoleDto);

        assertEquals(ROLE_CONSTANTS.ROLE_ADMIN, result.getRole());
    }

    @Test
    public void testFindActiveRole(){

        Role role = Role.builder()
                .role(ROLE_CONSTANTS.ROLE_MANAGER)
                .build();

        when(roleRepository.findActiveRole(ROLE_CONSTANTS.ROLE_MANAGER)).thenReturn(role);

        Role result = roleService.findActiveRole(ROLE_CONSTANTS.ROLE_MANAGER);
        assertEquals(result,role);
    }

    @Test
    public void testSave(){
        Role role = Role.builder()
                .role(ROLE_CONSTANTS.ROLE_MANAGER)
                .build();
        when(roleRepository.save(role)).thenReturn(role);
        Role result = roleService.save(role);
        assertEquals(result,role);
    }

    @Test
    public void testFindUsersInRole(){

        User user = User.builder()
                .email("test@bilgeadam.com")
                .authorizedRole(ROLE_CONSTANTS.ROLE_MANAGER)
                .build();

        List<User> userList = new ArrayList<>();
        userList.add(user);

        when(roleRepository.findUsersWithRole(ROLE_CONSTANTS.ROLE_MANAGER)).thenReturn(userList);
        List<User> result = roleService.findUsersInRole(ROLE_CONSTANTS.ROLE_MANAGER);

        assertEquals(1, result.size());
        assertTrue(result.contains(user));
    }
}
