package com.springboot.rest.api.server.payload;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class PageableDto {
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
