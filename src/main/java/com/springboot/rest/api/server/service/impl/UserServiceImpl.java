package com.springboot.rest.api.server.service.impl;

import com.springboot.rest.api.server.entity.User;
import com.springboot.rest.api.server.exception.ResourceNotFoundException;
import com.springboot.rest.api.server.payload.auth.UserDetailsDto;
import com.springboot.rest.api.server.payload.auth.UsersDto;
import com.springboot.rest.api.server.repository.UserRepository;
import com.springboot.rest.api.server.service.UserService;
import com.springboot.rest.api.server.utils.ObjectMapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public UsersDto findUsers(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<User> users = userRepository.findAll(pageable);

        return new UsersDto(users);
    }

    @Override
    public UserDetailsDto findUser(UUID uuid) {
        User user = findUserByUUID(uuid);
        return ObjectMapperUtil.map(user,UserDetailsDto.class);
    }

    @Override
    public UserDetailsDto updateUser(UserDetailsDto userDetailsDto, UUID uuid) {
        User user = findUserByUUID(uuid);
        user.setName(userDetailsDto.getName());
        user.setUsername(userDetailsDto.getUsername());
        user.setEmail(userDetailsDto.getEmail());
        User updateUser = userRepository.save(user);
        return ObjectMapperUtil.map(updateUser,UserDetailsDto.class);
    }

    @Override
    public void deleteUser(UUID uuid) {
        User user = findUserByUUID(uuid);
        userRepository.delete(user);
    }

    private User findUserById(long id){
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(User.class, "id", id));
    }

    private User findUserByUUID(UUID uuid){
        return userRepository.findByUuid(uuid).orElseThrow(() -> new ResourceNotFoundException(User.class, "uuid", uuid));
    }
}
