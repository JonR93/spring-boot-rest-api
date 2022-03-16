package com.springboot.rest.api.server.controller;

import com.springboot.rest.api.server.payload.RoleDto;
import com.springboot.rest.api.server.payload.RolesDto;
import com.springboot.rest.api.server.service.RoleService;
import com.springboot.rest.api.server.utils.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "CRUD Rest endpoints for Role resources")
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @ApiOperation(value = "Create Role")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody RoleDto roleDto){
        return new ResponseEntity<>(roleService.createRole(roleDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get Role by id")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @ApiOperation(value = "Get all Roles")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public RolesDto getAllRoles(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return roleService.findRoles(pageNo, pageSize, sortBy, sortDir);
    }

    @ApiOperation(value = "Update Role by id")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<RoleDto> updateRole(@Valid @RequestBody RoleDto roleDto, @PathVariable(name = "id") long id){
        RoleDto updatedRole = roleService.updateRole(roleDto, id);
        return new ResponseEntity<>(updatedRole, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete Role By id")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable(name = "id") long id){
        roleService.deleteRole(id);
        return new ResponseEntity<>("Role deleted successfully.", HttpStatus.OK);
    }
}
