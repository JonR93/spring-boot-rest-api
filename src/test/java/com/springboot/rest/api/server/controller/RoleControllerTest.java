package com.springboot.rest.api.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.rest.api.server.payload.RoleDto;
import com.springboot.rest.api.server.payload.RolesDto;
import com.springboot.rest.api.server.payload.UserDetailsDto;
import com.springboot.rest.api.server.service.RoleService;
import com.springboot.rest.api.server.utils.AppConstants;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(useDefaultFilters = false)
@Import(RoleController.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleService roleService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void createRole() throws Exception{
        RoleDto role = new RoleDto(null,"ROLE_TEST","Tester");

        given(roleService.createRole(role)).willReturn(new RoleDto(1L,"ROLE_TEST","Tester"));

        ResultActions response = mockMvc.perform(post("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role)));

        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(role.getName())))
                .andExpect(jsonPath("$.description", is(role.getDescription())));
    }

    @Test
    void getRoleById() throws Exception{
        RoleDto role = new RoleDto(1L,"ROLE_TEST","Tester");

        given(roleService.getRoleById(1L)).willReturn(role);

        ResultActions response = mockMvc.perform(get("/api/v1/roles/{id}", 1L));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(role.getName())))
                .andExpect(jsonPath("$.description", is(role.getDescription())));
    }

    @Test
    void getAllRoles() throws Exception{
        List<RoleDto> listOfRoles = new ArrayList<>();
        listOfRoles.add(new RoleDto(1L,"ROLE_TEST","Tester"));
        listOfRoles.add(new RoleDto(2L,"ROLE_USER","User"));
        listOfRoles.add(new RoleDto(3L,"ROLE_ADMIN","System Admin"));

        RolesDto roles = RolesDto.builder()
                .content(listOfRoles)
                .pageNo(0)
                .pageSize(listOfRoles.size())
                .totalElements(listOfRoles.size())
                .totalPages(1)
                .last(true)
                .build();

        given(roleService.findRoles(0,10, AppConstants.DEFAULT_SORT_BY,AppConstants.DEFAULT_SORT_DIRECTION)).willReturn(roles);

        ResultActions response = mockMvc.perform(get("/api/v1/roles",
                AppConstants.DEFAULT_PAGE_NUMBER,
                AppConstants.DEFAULT_PAGE_SIZE,
                AppConstants.DEFAULT_SORT_BY,
                AppConstants.DEFAULT_SORT_DIRECTION));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content.size()",
                        is(listOfRoles.size())));
    }

    @Test
    void updateRole() throws Exception{
        RoleDto updatedRole = new RoleDto(1L,"ROLE_TEST","Tester");

        given(roleService.updateRole(updatedRole,1L)).willReturn(updatedRole);

        ResultActions response = mockMvc.perform(put("/api/v1/roles/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRole)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(updatedRole.getName())))
                .andExpect(jsonPath("$.description", is(updatedRole.getDescription())));
    }

    @Test
    void deleteRole() throws Exception{
        willDoNothing().given(roleService).deleteRole(1L);
        ResultActions response = mockMvc.perform(delete("/api/v1/roles/{id}", 1L));
        response.andExpect(status().isOk())
                .andDo(print());
    }
}