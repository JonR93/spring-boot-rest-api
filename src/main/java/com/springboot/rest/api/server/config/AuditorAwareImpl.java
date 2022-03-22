package com.springboot.rest.api.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * This class implements the {@link AuditorAware} interface. It exposes the current user's username to the
 * for JPA auditing @CreatedBy and @LastModifiedBy annotations.
 */
@EnableJpaAuditing
@Configuration
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        //TODO: May want to change this in the future. Currently only know the user's email address in scope.

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return Optional.of("[System]");
        }

        return Optional.ofNullable(auth.getName());
    }
}
