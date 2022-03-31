package com.springboot.rest.api.server.config;

import com.springboot.rest.api.server.entity.User;
import com.springboot.rest.api.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static java.lang.Math.toIntExact;

import java.util.Optional;

/**
 * This class implements the {@link AuditorAware} interface. It exposes the current user's username to the
 * for JPA auditing @CreatedBy and @LastModifiedBy annotations.
 */
@EnableJpaAuditing
@Configuration
public class AuditorAwareImpl implements AuditorAware<Integer> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<Integer> getCurrentAuditor() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            User user = userRepository.findByUsernameOrEmail(auth.getName(), auth.getName()).orElse(null);
            if (user != null) {
                return Optional.of(toIntExact(user.getId()));
            }
        }
        return Optional.of(-1);
    }
}
