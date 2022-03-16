package com.springboot.rest.api.server.payload;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(description = "User model information")
@Data
public class UserDetailsDto {
    private String name;
    private String username;
    private String email;
}
