package com.springboot.rest.api.server.payload;

import com.springboot.rest.api.server.entity.Product;
import com.springboot.rest.api.server.utils.ObjectMapperUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProductsDto extends PageableDto{
    private List<ProductDto> content;

    public ProductsDto(Page<Product> page) {
        if(page!=null) {
            this.content = ObjectMapperUtil.mapAll(page.getContent(), ProductDto.class);
            setPageNo(page.getNumber());
            setPageSize(page.getSize());
            setTotalElements(page.getTotalElements());
            setTotalPages(page.getTotalPages());
            setLast(page.isLast());
        }
    }
}
