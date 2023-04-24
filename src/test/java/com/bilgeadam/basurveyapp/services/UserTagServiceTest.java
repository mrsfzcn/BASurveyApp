package com.bilgeadam.basurveyapp.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockitoAnnotations;

/**
 * @author Mert Cömertoğlu
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTagServiceTest {

    private UserTagService userTagService;

    @BeforeAll
    public void Init(){
        MockitoAnnotations.openMocks(this);
        userTagService= new UserTagService();
    }

    @Test
    public void testCreateTag(){

    }
}
