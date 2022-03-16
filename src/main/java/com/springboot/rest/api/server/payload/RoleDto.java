package com.springboot.rest.api.server.payload;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(description = "Role model information")
@Data
public class RoleDto {
    private long id;
    private String name;
}
