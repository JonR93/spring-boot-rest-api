package com.springboot.rest.api.server.payload;


import lombok.Data;

@Data
public class ProductImageDto {
    private long id;
    private String affiliateUrl;
    private Integer sequence;
    private boolean isPrimary;
}
