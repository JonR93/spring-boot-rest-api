package com.springboot.rest.api.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.rest.api.server.payload.CategoryDto;
import com.springboot.rest.api.server.payload.CategoriesDto;
import com.springboot.rest.api.server.service.CategoryService;
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
@Import(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void createCategory() throws Exception{
        CategoryDto category = new CategoryDto(null,"CATEGORY_TEST","Testing");

        given(categoryService.createCategory(category)).willReturn(new CategoryDto(1L,"CATEGORY_TEST","Testing"));

        ResultActions response = mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category)));

        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(category.getName())))
                .andExpect(jsonPath("$.description", is(category.getDescription())));
    }

    @Test
    void getCategoryById() throws Exception{
        CategoryDto category = new CategoryDto(1L,"CATEGORY_TEST","Testing");

        given(categoryService.getCategoryById(1L)).willReturn(category);

        ResultActions response = mockMvc.perform(get("/api/v1/categories/{id}", 1L));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(category.getName())))
                .andExpect(jsonPath("$.description", is(category.getDescription())));
    }

    @Test
    void getAllCategories() throws Exception{
        List<CategoryDto> listOfCategories = new ArrayList<>();
        listOfCategories.add(new CategoryDto(1L,"CAT1","Category 1"));
        listOfCategories.add(new CategoryDto(2L,"CAT2","Category 2"));
        listOfCategories.add(new CategoryDto(3L,"CAT3","Category 3"));

        CategoriesDto categories = CategoriesDto.builder()
                .content(listOfCategories)
                .pageNo(0)
                .pageSize(listOfCategories.size())
                .totalElements(listOfCategories.size())
                .totalPages(1)
                .last(true)
                .build();

        given(categoryService.findCategories(0,10, AppConstants.DEFAULT_SORT_BY,AppConstants.DEFAULT_SORT_DIRECTION)).willReturn(categories);

        ResultActions response = mockMvc.perform(get("/api/v1/categories",
                AppConstants.DEFAULT_PAGE_NUMBER,
                AppConstants.DEFAULT_PAGE_SIZE,
                AppConstants.DEFAULT_SORT_BY,
                AppConstants.DEFAULT_SORT_DIRECTION));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content.size()",
                        is(listOfCategories.size())));
    }

    @Test
    void updateCategory() throws Exception{
        CategoryDto updatedCategory = new CategoryDto(1L,"CAT1","Category 1");

        given(categoryService.updateCategory(updatedCategory,1L)).willReturn(updatedCategory);

        ResultActions response = mockMvc.perform(put("/api/v1/categories/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCategory)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(updatedCategory.getName())))
                .andExpect(jsonPath("$.description", is(updatedCategory.getDescription())));
    }

    @Test
    void deleteCategory() throws Exception{
        willDoNothing().given(categoryService).deleteCategory(1L);
        ResultActions response = mockMvc.perform(delete("/api/v1/categories/{id}", 1L));
        response.andExpect(status().isOk())
                .andDo(print());
    }
}