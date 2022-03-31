package com.springboot.rest.api.server.payload;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@ApiModel(description = "Category model information")
@Data
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
}
