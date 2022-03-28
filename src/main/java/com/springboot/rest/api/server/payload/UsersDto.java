package com.springboot.rest.api.server.payload;

import com.springboot.rest.api.server.entity.User;
import com.springboot.rest.api.server.utils.ObjectMapperUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UsersDto extends PageableDto{
    private List<UserDetailsDto> content;

    public UsersDto(Page<User> page) {
        if(page!=null) {
            this.content = ObjectMapperUtil.mapAll(page.getContent(), UserDetailsDto.class);
            setPageNo(page.getNumber());
            setPageSize(page.getSize());
            setTotalElements(page.getTotalElements());
            setTotalPages(page.getTotalPages());
            setLast(page.isLast());
        }
    }
}
