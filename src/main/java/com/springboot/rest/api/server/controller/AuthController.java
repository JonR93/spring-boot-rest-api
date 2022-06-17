package com.springboot.rest.api.server.controller;

import com.springboot.rest.api.server.payload.auth.*;
import com.springboot.rest.api.server.security.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Auth controller exposes register and login endpoints")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @ApiOperation(value = "Authenticates user login")
    @PostMapping("/login")
    public ResponseEntity<AuthenticatedUser> authenticateUser(@RequestBody LoginDto loginDto){
        AuthenticatedUser authenticatedUser = authService.login(loginDto.getUsernameOrEmail(),loginDto.getPassword());
        return new ResponseEntity<>(authenticatedUser, HttpStatus.OK);
    }

    @ApiOperation(value = "Registers a new user")
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody RegisterUserDto registerUserDto){
        if(authService.isUsernameTaken(registerUserDto.getUsername())){
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        if(authService.isEmailInUse(registerUserDto.getEmail())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }
        authService.registerUser(registerUserDto);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    @ApiOperation(value = "Check if an email exists in the system to send a password rest link to")
    @PostMapping("/forgot-password")
    public ResponseEntity<Object> processForgotPassword(@RequestBody PasswordForgotDto passwordForgotDto){
        if(!StringUtils.hasText(passwordForgotDto.getEmail())){
            return new ResponseEntity<>("No email provided", HttpStatus.BAD_REQUEST);
        }
        authService.processForgottenPassword(passwordForgotDto.getEmail());
        return new ResponseEntity<>("Password reset email sent", HttpStatus.OK);
    }

    @ApiOperation(value = "Reset password")
    @PostMapping("/reset-password")
    public ResponseEntity<Object> processResetPassword(@RequestBody PasswordResetDto passwordResetDto){
        if(!StringUtils.hasText(passwordResetDto.getToken())){
            return new ResponseEntity<>("Missing reset token", HttpStatus.BAD_REQUEST);
        }
        if(!(StringUtils.hasText(passwordResetDto.getPassword())
                && passwordResetDto.getPassword().equals(passwordResetDto.getConfirmPassword()))){
            return new ResponseEntity<>("Passwords do not match", HttpStatus.BAD_REQUEST);
        }
        authService.processResetPassword(passwordResetDto.getPassword(), passwordResetDto.getToken());
        return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
    }
}
