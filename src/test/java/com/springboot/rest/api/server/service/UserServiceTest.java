package com.springboot.rest.api.server.service;

import com.springboot.rest.api.server.entity.User;
import com.springboot.rest.api.server.payload.UserDetailsDto;
import com.springboot.rest.api.server.payload.UsersDto;
import com.springboot.rest.api.server.repository.UserRepository;
import com.springboot.rest.api.server.service.impl.UserServiceImpl;
import com.springboot.rest.api.server.utils.AppConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private UserServiceImpl userServiceIml;


    private User user;
    private UserDetailsDto userDetailsDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Jon")
                .username("WontonJon")
                .email("wontonjon@email.com")
                .password("password")
                .build();

        userDetailsDto = new ModelMapper().map(user, UserDetailsDto.class);
    }

    @Test
    void findUsers() {
        List<User> usersList = Collections.singletonList(user);

        Sort sort = AppConstants.DEFAULT_SORT_DIRECTION.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(AppConstants.DEFAULT_SORT_BY).ascending()
                : Sort.by(AppConstants.DEFAULT_SORT_BY).descending();

        Pageable pageable = PageRequest.of(0, 10, sort);

        Page<User> page = new PageImpl<User>(usersList);

        given(userRepository.findAll(pageable)).willReturn(page);
        given(mapper.map(user, UserDetailsDto.class)).willReturn(userDetailsDto);
        UsersDto users  = userServiceIml.findUsers(0,
                10,
                AppConstants.DEFAULT_SORT_BY,
                AppConstants.DEFAULT_SORT_DIRECTION);
        Assertions.assertThat(users.getPageNo()).isEqualTo(0);
        Assertions.assertThat(users.getPageSize()).isEqualTo(1);
        Assertions.assertThat(users.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(users.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(users.isLast()).isEqualTo(true);
    }

    @Test
    void findUser() {
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(mapper.map(user, UserDetailsDto.class)).willReturn(userDetailsDto);
        UserDetailsDto foundUser = userServiceIml.findUser(user.getId());
        assertThat(foundUser).isNotNull();
    }

    @Test
    void updateUser() {
        UserDetailsDto unsavedChanges = UserDetailsDto.builder()
                .username("test")
                .email("test@test.com")
                .name("test")
                .build();

        User savedUser = new User();
        savedUser.setName(unsavedChanges.getName());
        savedUser.setUsername(unsavedChanges.getUsername());
        savedUser.setEmail(unsavedChanges.getEmail());

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(savedUser);
        given(mapper.map(savedUser, UserDetailsDto.class)).willReturn(unsavedChanges);

        UserDetailsDto updatedUser = userServiceIml.updateUser(unsavedChanges,user.getId());

        Assertions.assertThat(updatedUser.getName()).isEqualTo("test");
        Assertions.assertThat(updatedUser.getEmail()).isEqualTo("test@test.com");
        Assertions.assertThat(updatedUser.getUsername()).isEqualTo("test");
    }

    @Test
    void deleteUser() {
        long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        willDoNothing().given(userRepository).delete(user);
        userServiceIml.deleteUser(userId);
        verify(userRepository, times(1)).delete(user);
    }
}