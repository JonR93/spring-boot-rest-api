package com.springboot.rest.api.server.payload;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(description = "Category model information")
@Data
public class CategoryDto {
    private String name;
}
