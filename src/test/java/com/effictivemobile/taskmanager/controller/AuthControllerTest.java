package com.effictivemobile.taskmanager.controller;

import com.effictivemobile.taskmanager.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class AuthControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testLogin() throws Exception {
        when(authService.login("test@example.com", "password123")).thenReturn("mock-jwt-token");

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .param("email", "test@example.com")
                        .param("password", "password123"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }
}
