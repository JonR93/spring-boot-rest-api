package com.springboot.rest.api.server.service.security;

import com.springboot.rest.api.server.entity.Role;
import com.springboot.rest.api.server.entity.User;
import com.springboot.rest.api.server.payload.auth.AuthenticatedUser;
import com.springboot.rest.api.server.payload.auth.RegisterUserDto;
import com.springboot.rest.api.server.repository.RoleRepository;
import com.springboot.rest.api.server.repository.UserRepository;
import com.springboot.rest.api.server.security.AuthService;
import com.springboot.rest.api.server.security.JwtTokenProvider;
import com.springboot.rest.api.server.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userServiceIml;

    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
    }

    @Test
    void isUsernameTaken() {
        String usernameTaken = "usernameTaken";
        String usernameNotTaken = "usernameNotTaken";

        //Username not taken
        given(userRepository.existsByUsername(usernameNotTaken)).willReturn(false);
        boolean isUsernameTaken = authService.isUsernameTaken(usernameNotTaken);
        Assertions.assertThat(isUsernameTaken).isFalse();

        //Username taken
        given(userRepository.existsByUsername(usernameTaken)).willReturn(true);
        isUsernameTaken = authService.isUsernameTaken(usernameTaken);
        Assertions.assertThat(isUsernameTaken).isTrue();
    }

    @Test
    void isEmailInUse() {
        String emailInUse = "emailInUse@myapi.com";
        String emailNotInUse = "emailNotInUse@myapi.com";

        //Username not taken
        given(userRepository.existsByUsername(emailNotInUse)).willReturn(false);
        boolean isEmailInUse = authService.isUsernameTaken(emailNotInUse);
        Assertions.assertThat(isEmailInUse).isFalse();

        //Username taken
        given(userRepository.existsByUsername(emailInUse)).willReturn(true);
        isEmailInUse = authService.isUsernameTaken(emailInUse);
        Assertions.assertThat(isEmailInUse).isTrue();
    }

    @Test
    void registerUser() {
        RegisterUserDto newUserDto = RegisterUserDto.builder()
                                        .username("wontonjon")
                                        .name("Jon")
                                        .email("wontonjon@myapi.com")
                                        .password("password")
                                        .build();

        String encodedPassword = passwordEncoder.encode(newUserDto.getPassword());

        User newUser = User.builder()
                .name(newUserDto.getName())
                .username(newUserDto.getUsername())
                .email(newUserDto.getEmail())
                .password(encodedPassword)
                .build();

        User savedUser = User.builder()
                .id(1)
                .name(newUserDto.getName())
                .username(newUserDto.getUsername())
                .email(newUserDto.getEmail())
                .password(encodedPassword)
                .build();

        given(userRepository.save(newUser)).willReturn(savedUser);

        User user = authService.registerUser(newUserDto);

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getId()).isPositive();
        Assertions.assertThat(user.getName()).isEqualTo(newUserDto.getName());
        Assertions.assertThat(user.getUsername()).isEqualTo(newUserDto.getUsername());
        Assertions.assertThat(user.getEmail()).isEqualTo(newUserDto.getEmail());
        Assertions.assertThat(user.getPassword()).isEqualTo(encodedPassword);
    }

    @Test
    void login() {
        String username = "username";
        String password = "password";
        User user = User.builder()
                .id(1)
                .username(username)
                .roles(Collections.singleton(new Role(1,"USER","User")))
                .build();

        Authentication authentication = Mockito.mock(Authentication.class);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        given(userRepository.findByUsernameOrEmail(username,username)).willReturn(Optional.ofNullable(user));
        given(authenticationManager.authenticate(authenticationToken)).willReturn(authentication);
        given(tokenProvider.generateToken(authentication,user)).willReturn("TOKEN");

        AuthenticatedUser authenticatedUser = authService.login(username,password);
        Assertions.assertThat(authenticatedUser).isNotNull();
        Assertions.assertThat(authenticatedUser.getId()).isEqualTo(user.getId());
        Assertions.assertThat(authenticatedUser.getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(authenticatedUser.getAccessToken()).isEqualTo("TOKEN");
    }

    @Test
    void processForgottenPassword() {
        //TODO: implement test case
    }

    @Test
    void processResetPassword() {
        //TODO: implement test case
    }
}