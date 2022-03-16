package com.springboot.rest.api.server.controller;

import com.springboot.rest.api.server.payload.UserDetailsDto;
import com.springboot.rest.api.server.payload.UsersDto;
import com.springboot.rest.api.server.service.UserService;
import com.springboot.rest.api.server.utils.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "CRUD Rest endpoints for User resources")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get User by id")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDetailsDto> getUserDetails(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(userService.findUser(id));
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

    @ApiOperation(value = "Update User by id")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<UserDetailsDto> updateUser(@Valid @RequestBody UserDetailsDto userDetailsDto, @PathVariable(name = "id") long id){
        UserDetailsDto updatedUser = userService.updateUser(userDetailsDto, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete User By id")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") long id){
        userService.deleteUser(id);
        return new ResponseEntity<>("User deleted successfully.", HttpStatus.OK);
    }
}
