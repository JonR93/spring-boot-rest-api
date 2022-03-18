package com.springboot.rest.api.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.rest.api.server.config.SecurityConfig;
import com.springboot.rest.api.server.entity.User;
import com.springboot.rest.api.server.payload.UserDetailsDto;
import com.springboot.rest.api.server.payload.UsersDto;
import com.springboot.rest.api.server.repository.UserRepository;
import com.springboot.rest.api.server.security.CustomUserDetailsService;
import com.springboot.rest.api.server.service.UserService;
import com.springboot.rest.api.server.utils.AppConstants;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(useDefaultFilters = false)
@Import(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private UserDetailsDto user1;
    private UserDetailsDto user2;
    private UserDetailsDto user3;
    private List<UserDetailsDto> listOfUsers;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        user1 = UserDetailsDto.builder()
                .name("User1")
                .username("User1")
                .email("User1@gmail.com")
                .build();

        user2 = UserDetailsDto.builder()
                .name("User2")
                .username("User2")
                .email("User2@gmail.com")
                .build();

        user3 = UserDetailsDto.builder()
                .name("User1")
                .username("User3")
                .email("User3@gmail.com")
                .build();

        listOfUsers = Arrays.asList(user1,user2,user3);
    }

    @Test
    void getUserDetails() throws Exception{
        UserDetailsDto user = UserDetailsDto.builder()
                .name("Jon")
                .username("wontonjon")
                .email("email@gmail.com")
                .build();

        given(userService.findUser(1L)).willReturn(user);

        ResultActions response = mockMvc.perform(get("/api/v1/users/{id}", 1L));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void getAllUsers() throws Exception{
        UsersDto users = UsersDto.builder()
                .content(listOfUsers)
                .pageNo(0)
                .pageSize(listOfUsers.size())
                .totalElements(listOfUsers.size())
                .totalPages(1)
                .last(true)
                .build();

        given(userService.findUsers(0,10,AppConstants.DEFAULT_SORT_BY,AppConstants.DEFAULT_SORT_DIRECTION)).willReturn(users);

        ResultActions response = mockMvc.perform(get("/api/v1/users",
                AppConstants.DEFAULT_PAGE_NUMBER,
                AppConstants.DEFAULT_PAGE_SIZE,
                AppConstants.DEFAULT_SORT_BY,
                AppConstants.DEFAULT_SORT_DIRECTION));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content.size()",
                        is(listOfUsers.size())));
    }

    @Test
    void updateUser() throws Exception{
        UserDetailsDto updatedUser = UserDetailsDto.builder()
                .name("Jonathan")
                .email("email@gmail.com")
                .build();

        given(userService.updateUser(updatedUser,1L)).willReturn(updatedUser);

        ResultActions response = mockMvc.perform(put("/api/v1/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(updatedUser.getName())))
                .andExpect(jsonPath("$.email", is(updatedUser.getEmail())));
    }

    @Test
    void deleteUser() throws Exception{
        willDoNothing().given(userService).deleteUser(1L);
        ResultActions response = mockMvc.perform(delete("/api/v1/users/{id}", 1L));
        response.andExpect(status().isOk())
                .andDo(print());
    }
}