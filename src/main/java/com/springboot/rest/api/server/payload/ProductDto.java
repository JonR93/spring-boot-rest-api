package com.springboot.rest.api.server.payload;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.springboot.rest.api.server.utils.BigDecimalSerializer;
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
    private Long id;
    private String name;
    private String description;
    private String review;
    private String affiliateLinkUrl;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal price;
    private double rating;
    private int impressions;
    private boolean featured;
    private Set<CategoryDto> categories;
    private ProductImageDto primaryProductImage;
    private Set<ProductImageDto> productImages;
}
