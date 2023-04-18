package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.Manager;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.repositories.ManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Mert Cömertoğlu
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ManagerServiceTest {

    @Mock
    private ManagerRepository managerRepository;
    
    private ManagerService managerService;

    @BeforeEach
    public void Init() {
        MockitoAnnotations.openMocks(this);
        managerService = new ManagerService(managerRepository);
    }

    @Test
    public void testCreateManager() {

        User user = User.builder()
                .email("ad.soyad@bilgeadam.com")
                .password("12345678")
                .firstName("ad")
                .lastName("soyad")
                .build();

        Manager manager = new Manager();
        manager.setUser(user);

        when(managerRepository.save(manager)).thenReturn(manager);
        Boolean result = managerService.createManager(manager);
        verify(managerRepository).save(manager);
        assertEquals(true, result);
    }
}
