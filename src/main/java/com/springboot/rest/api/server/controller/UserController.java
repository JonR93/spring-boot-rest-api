package com.springboot.rest.api.server.controller;

import com.springboot.rest.api.server.payload.auth.UserDetailsDto;
import com.springboot.rest.api.server.payload.auth.UsersDto;
import com.springboot.rest.api.server.service.UserService;
import com.springboot.rest.api.server.utils.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Api(value = "CRUD Rest endpoints for User resources")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get User by uuid")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/{uuid}")
    public ResponseEntity<UserDetailsDto> getUserDetails(@PathVariable(value = "uuid") UUID uuid){
        return ResponseEntity.ok(userService.findUser(uuid));
    }

    @ApiOperation(value = "Get all Users")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public UsersDto getAllUsers(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return userService.findUsers(pageNo, pageSize, sortBy, sortDir);
    }

    @ApiOperation(value = "Update User by uuid")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{uuid}")
    public ResponseEntity<UserDetailsDto> updateUser(@Valid @RequestBody UserDetailsDto userDetailsDto, @PathVariable(name = "uuid") UUID uuid){
        UserDetailsDto updatedUser = userService.updateUser(userDetailsDto, uuid);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete User By uuid")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "uuid") UUID uuid){
        userService.deleteUser(uuid);
        return new ResponseEntity<>("User deleted successfully.", HttpStatus.OK);
    }
}
