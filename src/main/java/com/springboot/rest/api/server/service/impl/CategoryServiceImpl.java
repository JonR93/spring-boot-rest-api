package com.springboot.rest.api.server.service.impl;

import com.springboot.rest.api.server.entity.Category;
import com.springboot.rest.api.server.exception.ResourceNotFoundException;
import com.springboot.rest.api.server.payload.CategoriesDto;
import com.springboot.rest.api.server.payload.CategoryDto;
import com.springboot.rest.api.server.repository.CategoryRepository;
import com.springboot.rest.api.server.service.CategoryService;
import com.springboot.rest.api.server.utils.ObjectMapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = ObjectMapperUtil.map(categoryDto,Category.class);
        Category newCategory = categoryRepository.save(category);
        return ObjectMapperUtil.map(newCategory,CategoryDto.class);
    }

    @Override
    public CategoryDto getCategoryById(long id) {
        Category category = findCategoryById(id);
        return ObjectMapperUtil.map(category,CategoryDto.class);
    }

    @Override
    public CategoryDto getCategoryByName(String name) {
        Category category = findCategoryByName(name);
        return ObjectMapperUtil.map(category,CategoryDto.class);
    }

    @Override
    public CategoriesDto findCategories(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Category> users = categoryRepository.findAll(pageable);

        return new CategoriesDto(users);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, long id) {
        Category category = findCategoryById(id);
        category.setName(categoryDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        return ObjectMapperUtil.map(updatedCategory,CategoryDto.class);
    }

    @Override
    public void deleteCategory(long id) {
        Category category = findCategoryById(id);
        categoryRepository.delete(category);
    }

    private Category findCategoryById(long id){
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    private Category findCategoryByName(String name){
        return categoryRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Category", "name", name));
    }
}
