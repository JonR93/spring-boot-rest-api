package com.springboot.rest.api.server.payload;

import lombok.Data;

@Data
public class RegisterUserDto {
    private String name;
    private String username;
    private String email;
    private String password;
}
