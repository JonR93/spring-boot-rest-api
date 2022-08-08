package com.springboot.rest.api.server.service;

import com.springboot.rest.api.server.payload.auth.UserDetailsDto;
import com.springboot.rest.api.server.payload.auth.UsersDto;

import java.util.UUID;

public interface UserService {
    UsersDto findUsers(int pageNo, int pageSize, String sortBy, String sortDir);
    UserDetailsDto findUser(UUID uuid);
    UserDetailsDto updateUser(UserDetailsDto userDetailsDto, UUID uuid);
    void deleteUser(UUID uuid);
}
