package com.springboot.rest.api.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.rest.api.server.entity.User;
import com.springboot.rest.api.server.payload.LoginDto;
import com.springboot.rest.api.server.payload.PasswordForgotDto;
import com.springboot.rest.api.server.payload.PasswordResetDto;
import com.springboot.rest.api.server.payload.RegisterUserDto;
import com.springboot.rest.api.server.security.AuthService;
import com.springboot.rest.api.server.utils.ObjectMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private RegisterUserDto registerUserDto;
    private LoginDto loginDto;
    private PasswordForgotDto passwordForgotDto;
    private PasswordResetDto passwordResetDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void authenticateUser() throws Exception {
        String token = "TOKEN";
        loginDto = new LoginDto("wontonjon","password");

        given(authService.login(loginDto.getUsernameOrEmail(),loginDto.getPassword())).willReturn(token);

        ResultActions response = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.accessToken", is(token)))
                .andExpect(jsonPath("$.tokenType", is("Bearer")));
    }

    @Test
    void registerUser() throws Exception {
        registerUserDto = RegisterUserDto.builder()
                .username("wontonjon")
                .name("Jon")
                .email("test@myrestapi.com")
                .password("password")
                .build();

        User registeredUser = ObjectMapperUtil.map(registerUserDto,User.class);

        given(authService.isUsernameTaken(registerUserDto.getUsername())).willReturn(false);
        given(authService.isEmailInUse(registerUserDto.getEmail())).willReturn(false);
        given(authService.registerUser(registerUserDto)).willReturn(registeredUser);

        ResultActions response = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerUserDto)));

        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void registerUserFailsWhenUsernameIsTaken() throws Exception {
        registerUserDto = RegisterUserDto.builder()
                .username("wontonjon")
                .name("Jon")
                .email("test@myrestapi.com")
                .password("password")
                .build();

        User registeredUser = ObjectMapperUtil.map(registerUserDto,User.class);

        given(authService.isUsernameTaken(registerUserDto.getUsername())).willReturn(true);
        given(authService.isEmailInUse(registerUserDto.getEmail())).willReturn(false);
        given(authService.registerUser(registerUserDto)).willReturn(registeredUser);

        ResultActions response = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerUserDto)));

        response.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void registerUserFailsWhenEmailIsTaken() throws Exception {
        registerUserDto = RegisterUserDto.builder()
                .username("wontonjon")
                .name("Jon")
                .email("test@myrestapi.com")
                .password("password")
                .build();

        User registeredUser = ObjectMapperUtil.map(registerUserDto,User.class);

        given(authService.isUsernameTaken(registerUserDto.getUsername())).willReturn(false);
        given(authService.isEmailInUse(registerUserDto.getEmail())).willReturn(true);
        given(authService.registerUser(registerUserDto)).willReturn(registeredUser);

        ResultActions response = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerUserDto)));

        response.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void processForgotPassword() throws Exception{
        passwordForgotDto = new PasswordForgotDto();
        passwordForgotDto.setEmail("test@myrestapi.com");

        willDoNothing().given(authService).processForgottenPassword(passwordForgotDto.getEmail());

        ResultActions response = mockMvc.perform(post("/api/v1/auth/forgot-password")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordForgotDto)));

        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void processForgotPasswordBadRequest() throws Exception{
        passwordForgotDto = new PasswordForgotDto();
        passwordForgotDto.setEmail("");

        willDoNothing().given(authService).processForgottenPassword(passwordForgotDto.getEmail());

        ResultActions response = mockMvc.perform(post("/api/v1/auth/forgot-password")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordForgotDto)));

        response.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void processResetPassword() throws Exception{
        passwordResetDto = new PasswordResetDto("password","password","TOKEN");

        willDoNothing().given(authService).processResetPassword(passwordResetDto.getPassword(), passwordResetDto.getToken());

        ResultActions response = mockMvc.perform(post("/api/v1/auth/reset-password")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordResetDto)));

        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void processResetPasswordPasswordsDontMatch() throws Exception{
        passwordResetDto = new PasswordResetDto("password","differentPassword","TOKEN");

        willDoNothing().given(authService).processResetPassword(passwordResetDto.getPassword(), passwordResetDto.getToken());

        ResultActions response = mockMvc.perform(post("/api/v1/auth/reset-password")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordResetDto)));

        response.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void processResetPasswordMissingResetToken() throws Exception{
        passwordResetDto = new PasswordResetDto("password","differentPassword","");

        willDoNothing().given(authService).processResetPassword(passwordResetDto.getPassword(), passwordResetDto.getToken());

        ResultActions response = mockMvc.perform(post("/api/v1/auth/reset-password")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordResetDto)));

        response.andExpect(status().isBadRequest())
                .andDo(print());
    }
}