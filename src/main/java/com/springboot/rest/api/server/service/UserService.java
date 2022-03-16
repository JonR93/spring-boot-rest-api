package com.springboot.rest.api.server.service;

import com.springboot.rest.api.server.payload.UserDetailsDto;
import com.springboot.rest.api.server.payload.UsersDto;

public interface UserService {
    UsersDto findUsers(int pageNo, int pageSize, String sortBy, String sortDir);
    UserDetailsDto findUser(long id);
    UserDetailsDto updateUser(UserDetailsDto userDetailsDto, long id);
    void deleteUser(long id);
}
