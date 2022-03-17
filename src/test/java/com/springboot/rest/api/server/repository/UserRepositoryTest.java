package com.springboot.rest.api.server.repository;

import com.springboot.rest.api.server.entity.Role;
import com.springboot.rest.api.server.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User user;
    private Role roleAdmin;

    @BeforeEach
    void setUp() {
        roleAdmin = Role.builder()
                .name("ROLE_ADMIN")
                .build();
        roleAdmin = roleRepository.save(roleAdmin);

        user = User.builder()
            .name("Jon")
            .username("WontonJon")
            .email("wontonjon@email.com")
            .password("password")
            .roles(Collections.singleton(roleAdmin))
            .build();
        userRepository.save(user);
    }

    @Test
    void findByEmail() {
        User foundUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getEmail()).isNotNull();
        Assertions.assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void findByUsernameOrEmail() {
        User foundUser = userRepository.findByUsernameOrEmail(null,user.getEmail()).orElse(null);
        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getEmail()).isNotNull();
        Assertions.assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());

        foundUser = userRepository.findByUsernameOrEmail(user.getUsername(),null).orElse(null);
        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getUsername()).isNotNull();
        Assertions.assertThat(foundUser.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void findByUsername() {
        User foundUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getUsername()).isNotNull();
        Assertions.assertThat(foundUser.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void existsByUsername() {
        boolean foundUser = userRepository.existsByUsername(user.getUsername());
        Assertions.assertThat(foundUser).isTrue();
    }

    @Test
    void existsByEmail() {
        boolean foundUser = userRepository.existsByEmail(user.getEmail());
        Assertions.assertThat(foundUser).isTrue();
    }

    @Test
    void updatePassword() {
        userRepository.updatePassword("newPassword",user.getId());
        User modifiedUser = userRepository.findById(user.getId()).orElse(null);
        Assertions.assertThat(modifiedUser).isNotNull();
        Assertions.assertThat(modifiedUser.getId()).isEqualTo(user.getId());
        Assertions.assertThat(modifiedUser.getPassword()).isEqualTo("newPassword");
    }

    @Test
    void delete() {
        User foundUser = userRepository.findById(user.getId()).orElse(null);
        Assertions.assertThat(foundUser).isNotNull();
        userRepository.delete(foundUser);
        foundUser = userRepository.findById(user.getId()).orElse(null);
        Assertions.assertThat(foundUser).isNull();
    }
}