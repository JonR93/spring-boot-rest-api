package com.springboot.rest.api.server.controller;

import com.springboot.rest.api.server.entity.Mail;
import com.springboot.rest.api.server.entity.PasswordResetToken;
import com.springboot.rest.api.server.entity.User;
import com.springboot.rest.api.server.exception.ResourceNotFoundException;
import com.springboot.rest.api.server.payload.*;
import com.springboot.rest.api.server.repository.PasswordResetTokenRepository;
import com.springboot.rest.api.server.repository.RoleRepository;
import com.springboot.rest.api.server.repository.UserRepository;
import com.springboot.rest.api.server.security.JwtTokenProvider;
import com.springboot.rest.api.server.service.EmailService;
import com.springboot.rest.api.server.utils.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.UUID;

@Api(value = "Auth controller exposes register and login endpoints")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @ApiOperation(value = "Authenticates user login")
    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // get token form tokenProvider
        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

    @ApiOperation(value = "Registers a new user")
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody RegisterUserDto registerUserDto){
        boolean usernameTaken = userRepository.existsByUsername(registerUserDto.getUsername());
        if(usernameTaken){
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        boolean emailInUse = userRepository.existsByEmail(registerUserDto.getEmail());
        if(emailInUse){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                .name(registerUserDto.getName())
                .username(registerUserDto.getUsername())
                .email(registerUserDto.getEmail())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .build();

        roleRepository.findByName("ROLE_USER").ifPresent(role -> user.setRoles(Collections.singleton(role)));
        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

    }

    @ApiOperation(value = "Check if an email exists in the system to send a password rest link to")
    @PostMapping("/forgot-password")
    public ResponseEntity<Object> processForgotPassword(@RequestBody PasswordForgotDto passwordForgotDto){
        String emailAddress = passwordForgotDto.getEmail();

        // Confirm a user exists with this email address
        User user = userRepository.findByEmail(emailAddress).orElseThrow(() -> new ResourceNotFoundException("User", "email", emailAddress));

        // Generate password reset token
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(AppConstants.DEFAULT_PASSWORD_RESET_TOKEN_EXPIRATION_MINUTES);
        passwordResetTokenRepository.save(token);

        // Generate email
        Mail mail = new Mail();
        mail.setSendFrom("no-reply@springbootrestapi.com");
        mail.setSendTo(user.getEmail());
        mail.setSubject("Password reset request");
        // TODO: format email
        mail.setBody(token.getToken());

        // Send email
        emailService.sendEmail(mail);

        return new ResponseEntity<>("Password reset email sent", HttpStatus.OK);
    }

    @ApiOperation(value = "Reset password")
    @PostMapping("/reset-password")
    public ResponseEntity<Object> processResetPassword(@RequestBody PasswordResetDto passwordResetDto){
        String token = passwordResetDto.getToken();
        String newPassword = passwordResetDto.getPassword();
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        User user = passwordResetToken.getUser();
        String updatedPassword = passwordEncoder.encode(newPassword);
        userRepository.updatePassword(updatedPassword, user.getId());
        passwordResetTokenRepository.delete(passwordResetToken);
        return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
    }
}
