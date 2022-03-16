package com.springboot.rest.api.server.service;

import com.springboot.rest.api.server.payload.RoleDto;
import com.springboot.rest.api.server.payload.RolesDto;

public interface RoleService {
    RoleDto createRole(RoleDto roleDto);
    RoleDto getRoleById(long id);
    RoleDto getRoleByName(String name);
    RolesDto findRoles(int pageNo, int pageSize, String sortBy, String sortDir);
    RoleDto updateRole(RoleDto role, long id);
    void deleteRole(long id);
}
