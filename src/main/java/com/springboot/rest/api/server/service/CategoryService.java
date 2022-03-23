package com.springboot.rest.api.server.service;

import com.springboot.rest.api.server.payload.CategoriesDto;
import com.springboot.rest.api.server.payload.CategoryDto;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto getCategoryById(long id);
    CategoryDto getCategoryByName(String name);
    CategoriesDto findCategories(int pageNo, int pageSize, String sortBy, String sortDir);
    CategoryDto updateCategory(CategoryDto categoryDto, long id);
    void deleteCategory(long id);
}
