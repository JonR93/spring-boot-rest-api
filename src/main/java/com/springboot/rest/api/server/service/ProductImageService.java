package com.springboot.rest.api.server.service;

import com.springboot.rest.api.server.payload.ProductImageDto;

import java.util.List;

public interface ProductImageService {
    ProductImageDto addImageToProduct(Long productId, ProductImageDto productImageDto);
    List<ProductImageDto> getImagesForProduct(Long productId);
    ProductImageDto getImage(Long productId, Long productImageId);
    ProductImageDto updateImage(Long productId, Long productImageId, ProductImageDto productImageDto);
    void deleteImage(Long productId, Long productImageId);
}
