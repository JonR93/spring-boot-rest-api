package com.springboot.rest.api.server.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.springboot.rest.api.server.utils.BigDecimalSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private String review;
    private String affiliateLinkUrl;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal price;
    private double rating;
    private int impressions;
    private boolean featured;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> productImages = new TreeSet<>();


    @Transient
    public ProductImage getPrimaryProductImage() {
        return productImages.stream()
                .filter(ProductImage::isPrimary)
                .findFirst()
                .orElse(null);
    }

    public void setPrimaryProductImage(ProductImage primaryProductImage) {
        if(primaryProductImage!=null){
            primaryProductImage.setPrimary(true);
            productImages.stream()
                    .filter(image -> !image.equals(primaryProductImage))
                    .forEach(productImage -> productImage.setPrimary(false));
        }
    }

    public void addImage(ProductImage productImage){
        if(productImage!=null){
            if(productImages==null){
                productImages = new TreeSet<>();
            }
            productImages.add(productImage);
            if(productImages.size()==1){
                setPrimaryProductImage(productImage);
            }
        }
    }
}
