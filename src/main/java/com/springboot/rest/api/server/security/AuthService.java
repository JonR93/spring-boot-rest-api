package com.springboot.rest.api.server.security;

import com.springboot.rest.api.server.entity.Mail;
import com.springboot.rest.api.server.entity.PasswordResetToken;
import com.springboot.rest.api.server.entity.Role;
import com.springboot.rest.api.server.entity.User;
import com.springboot.rest.api.server.exception.ResourceNotFoundException;
import com.springboot.rest.api.server.payload.auth.AuthenticatedUser;
import com.springboot.rest.api.server.payload.auth.RegisterUserDto;
import com.springboot.rest.api.server.repository.PasswordResetTokenRepository;
import com.springboot.rest.api.server.repository.RoleRepository;
import com.springboot.rest.api.server.repository.UserRepository;
import com.springboot.rest.api.server.service.EmailService;
import com.springboot.rest.api.server.utils.AppConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    private static final String DEFAULT_USER_ROLE = "ROLE_USER";

    /**
     * Is this username taken?
     * @param username
     * @return true if a user already exists with this username
     */
    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Is this email address already in use?
     * @param emailAddress
     * @return true if a user already exists with this email address
     */
    public boolean isEmailInUse(String emailAddress) {
        return userRepository.existsByEmail(emailAddress);
    }

    /**
     * Parse out the user properties from the DTO to build a new user.
     * Set a default user role before saving.
     * @param registerUserDto
     * @return a newly registered user
     */
    public User registerUser(RegisterUserDto registerUserDto){
        User user = User.builder()
                .name(registerUserDto.getName())
                .username(registerUserDto.getUsername())
                .email(registerUserDto.getEmail())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .build();
        roleRepository.findByName(DEFAULT_USER_ROLE).ifPresent(role -> user.setRoles(Collections.singleton(role)));
        User newUser = userRepository.save(user);
        log.info(String.format("[%s] has successfully registered",registerUserDto.getUsername()));
        return newUser;
    }

    /**
     * Performs user login authentication.
     * @param usernameOrEmail
     * @param password
     * @return authenticated user
     */
    public AuthenticatedUser login(String usernameOrEmail, String password){
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail).orElse(null);
        if(user != null){
            Authentication authentication = authenticateUser(usernameOrEmail,password);
            String accessToken = tokenProvider.generateToken(authentication, user);
            Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

            AuthenticatedUser authenticatedUser = new AuthenticatedUser();
            authenticatedUser.setId(user.getId());
            authenticatedUser.setName(user.getName());
            authenticatedUser.setUsername(user.getUsername());
            authenticatedUser.setEmail(user.getEmail());
            authenticatedUser.setRoles(roles);
            authenticatedUser.setAccessToken(accessToken);

            return authenticatedUser;
        }
        return null;
    }

    /**
     * Authenticates the user's login details and adds them to the security context
     * @param usernameOrEmail
     * @param password
     * @return Authentication object
     */
    private Authentication authenticateUser(String usernameOrEmail, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usernameOrEmail, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private PasswordResetToken generatePasswordResetToken(User user){
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(AppConstants.DEFAULT_PASSWORD_RESET_TOKEN_EXPIRATION_MINUTES);
        return passwordResetTokenRepository.save(token);
    }

    /**
     * Find the user by their email address and generate a PasswordResetToken to be emailed.
     * @param emailAddress - user's email address that will receive the password reset token
     */
    public void processForgottenPassword(String emailAddress){
        User user = userRepository.findByEmail(emailAddress).orElseThrow(() -> new ResourceNotFoundException(User.class, "email", emailAddress));
        PasswordResetToken passwordResetToken = generatePasswordResetToken(user);
        Mail mail = Mail.builder()
                .sendFrom(AppConstants.NO_REPLY_EMAIL_ADDRESS)
                .sendTo("jon.ruel93@gmail.com")
                .subject("Password reset request")
                .body(passwordResetToken.getToken())
                .build();
        emailService.sendEmail(mail);
    }

    /**
     * Given the password reset token and a new password, validate the token and then update user's password.
     * Delete the password reset token after use.
     * @param password
     * @param token
     */
    public void processResetPassword(String password, String token){
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException(PasswordResetToken.class, "token", token));
        User user = passwordResetToken.getUser();
        String updatedPassword = passwordEncoder.encode(password);
        userRepository.updatePassword(updatedPassword, user.getId());
        passwordResetTokenRepository.delete(passwordResetToken);
    }

}
