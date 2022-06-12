package com.springboot.rest.api.server.security;

import com.springboot.rest.api.server.entity.Mail;
import com.springboot.rest.api.server.entity.PasswordResetToken;
import com.springboot.rest.api.server.entity.User;
import com.springboot.rest.api.server.exception.ResourceNotFoundException;
import com.springboot.rest.api.server.payload.RegisterUserDto;
import com.springboot.rest.api.server.repository.PasswordResetTokenRepository;
import com.springboot.rest.api.server.repository.RoleRepository;
import com.springboot.rest.api.server.repository.UserRepository;
import com.springboot.rest.api.server.service.EmailService;
import com.springboot.rest.api.server.utils.AppConstants;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

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
        roleRepository.findByName("ROLE_USER").ifPresent(role -> user.setRoles(Collections.singleton(role)));
        return userRepository.save(user);
    }

    /**
     * Performs user login authentication.
     * @param usernameOrEmail
     * @param password
     * @return authentication token
     */
    public String login(String usernameOrEmail, String password){
        Authentication userAuthentication = authenticateUser(usernameOrEmail,password);
        return generateJwtToken(userAuthentication);
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

    /**
     * Generates a unique JWT token based on the user's authentication
     * @param authentication
     * @return JWT token string
     */
    private String generateJwtToken(Authentication authentication){
        User authenticatedUser = userRepository.findByUsernameOrEmail(authentication.getName(),authentication.getName()).orElse(null);
        return tokenProvider.generateToken(authentication, authenticatedUser);
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
