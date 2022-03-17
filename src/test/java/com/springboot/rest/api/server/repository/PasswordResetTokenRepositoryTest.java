package com.springboot.rest.api.server.repository;

import com.springboot.rest.api.server.entity.PasswordResetToken;
import com.springboot.rest.api.server.entity.Role;
import com.springboot.rest.api.server.entity.User;
import com.springboot.rest.api.server.utils.AppConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;

@DataJpaTest
class PasswordResetTokenRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    private static final String PASSWORD_RESET_TOKEN = "token";
    private User user;
    private Role roleAdmin;
    private PasswordResetToken passwordResetToken;

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
        user = userRepository.save(user);

        passwordResetToken = PasswordResetToken.builder()
                .user(user)
                .token(PASSWORD_RESET_TOKEN)
                .build();
        passwordResetToken.setExpiryDate(AppConstants.DEFAULT_PASSWORD_RESET_TOKEN_EXPIRATION_MINUTES);
        passwordResetToken = passwordResetTokenRepository.save(passwordResetToken);
    }

    @Test
    void findByToken() {
        PasswordResetToken token = passwordResetTokenRepository.findByToken(PASSWORD_RESET_TOKEN);
        Assertions.assertThat(token).isNotNull();
        Assertions.assertThat(token.getToken()).isEqualTo(PASSWORD_RESET_TOKEN);
        Assertions.assertThat(token.getUser()).isEqualTo(user);
    }
}