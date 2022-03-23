package com.springboot.rest.api.server.payload;

import com.springboot.rest.api.server.entity.Category;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@ApiModel(description = "Product model information")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String name;
    private String description;
    private String review;
    private String affiliateLinkUrl;
    private BigDecimal price;
    private double rating;
    private int impressions;
    private boolean featured;
    private Set<Category> categories;
}
