package com.springboot.rest.api.server.controller;

import com.springboot.rest.api.server.payload.CategoriesDto;
import com.springboot.rest.api.server.payload.CategoryDto;
import com.springboot.rest.api.server.service.CategoryService;
import com.springboot.rest.api.server.utils.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "CRUD Rest endpoints for Category resources")
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiOperation(value = "Create Category")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        return new ResponseEntity<>(categoryService.createCategory(categoryDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get Category by id")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @ApiOperation(value = "Get all Categories")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public CategoriesDto getAllCategories(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return categoryService.findCategories(pageNo, pageSize, sortBy, sortDir);
    }

    @ApiOperation(value = "Update Category by id")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable(name = "id") long id){
        CategoryDto updatedCategory = categoryService.updateCategory(categoryDto, id);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete Category By id")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable(name = "id") long id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>("Category deleted successfully.", HttpStatus.OK);
    }
}
