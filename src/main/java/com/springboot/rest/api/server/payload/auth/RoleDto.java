package com.springboot.rest.api.server.payload.auth;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@ApiModel(description = "Role model information")
@Data
@AllArgsConstructor
public class RoleDto {
    private Long id;
    private String name;
    private String description;
}
