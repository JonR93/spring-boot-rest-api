package com.springboot.rest.api.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.rest.api.server.security.AuthService;
import com.springboot.rest.api.server.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(useDefaultFilters = false)
@Import(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
    }

    @Test
    void authenticateUser() {
    }

    @Test
    void registerUser() {
    }

    @Test
    void processForgotPassword() {
    }

    @Test
    void processResetPassword() {
    }
}