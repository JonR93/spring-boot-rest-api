package com.springboot.rest.api.server.payload.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserDto {
    private String name;
    private String username;
    private String email;
    private String password;
}
