package com.springboot.rest.api.server.payload.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * When u ser logs in and is authenticated, send back some information about the user and their access token
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticatedUser {
    private UUID uuid;
    private String name;
    private String username;
    private String email;
    private Set<String> roles = new HashSet<>();
    private String accessToken;
}
