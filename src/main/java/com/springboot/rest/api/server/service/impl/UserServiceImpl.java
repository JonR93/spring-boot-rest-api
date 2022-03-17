package com.springboot.rest.api.server.service.impl;

import com.springboot.rest.api.server.entity.User;
import com.springboot.rest.api.server.exception.ResourceNotFoundException;
import com.springboot.rest.api.server.payload.UserDetailsDto;
import com.springboot.rest.api.server.payload.UsersDto;
import com.springboot.rest.api.server.repository.UserRepository;
import com.springboot.rest.api.server.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper mapper){
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public UsersDto findUsers(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<User> users = userRepository.findAll(pageable);

        // get content for page object
        List<User> listOfUsers = users.getContent();

        List<UserDetailsDto> content = listOfUsers.stream().map(this::mapToDTO).collect(Collectors.toList());

        UsersDto usersResponse = new UsersDto();
        usersResponse.setContent(content);
        usersResponse.setPageNo(users.getNumber());
        usersResponse.setPageSize(users.getSize());
        usersResponse.setTotalElements(users.getTotalElements());
        usersResponse.setTotalPages(users.getTotalPages());
        usersResponse.setLast(users.isLast());

        return usersResponse;
    }

    @Override
    public UserDetailsDto findUser(long id) {
        User user = findUserById(id);
        return mapToDTO(user);
    }

    @Override
    public UserDetailsDto updateUser(UserDetailsDto userDetailsDto, long id) {
        User user = findUserById(id);
        user.setName(userDetailsDto.getName());
        user.setUsername(userDetailsDto.getUsername());
        user.setEmail(userDetailsDto.getEmail());
        User updateUser = userRepository.save(user);
        return mapToDTO(updateUser);
    }

    @Override
    public void deleteUser(long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    private User findUserById(long id){
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    // convert Entity into DTO
    private UserDetailsDto mapToDTO(User user){
        return mapper.map(user, UserDetailsDto.class);
    }
}
