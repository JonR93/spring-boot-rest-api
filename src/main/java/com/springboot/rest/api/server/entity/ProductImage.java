package com.springboot.rest.api.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "productImages")
public class ProductImage extends AuditableEntity implements Serializable, Comparable<ProductImage> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String affiliateUrl;
    private Integer sequence;
    private boolean isPrimary;

    @Override
    public int compareTo(ProductImage other) {
        if(other == null){
            return 1;
        }
        return new CompareToBuilder()
                .append(this.sequence, other.sequence)
                .build();
    }
}
