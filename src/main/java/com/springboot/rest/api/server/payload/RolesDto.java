package com.springboot.rest.api.server.payload;

import com.springboot.rest.api.server.entity.Role;
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
public class RolesDto extends PageableDto{
    private List<RoleDto> content;

    public RolesDto(Page<Role> page) {
        if(page!=null) {
            this.content = ObjectMapperUtil.mapAll(page.getContent(), RoleDto.class);
            setPageNo(page.getNumber());
            setPageSize(page.getSize());
            setTotalElements(page.getTotalElements());
            setTotalPages(page.getTotalPages());
            setLast(page.isLast());
        }
    }
}
