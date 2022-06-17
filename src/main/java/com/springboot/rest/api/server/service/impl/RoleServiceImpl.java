package com.springboot.rest.api.server.service.impl;

import com.springboot.rest.api.server.entity.Role;
import com.springboot.rest.api.server.exception.ResourceNotFoundException;
import com.springboot.rest.api.server.payload.auth.RoleDto;
import com.springboot.rest.api.server.payload.auth.RolesDto;
import com.springboot.rest.api.server.repository.RoleRepository;
import com.springboot.rest.api.server.service.RoleService;
import com.springboot.rest.api.server.utils.ObjectMapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        Role role = ObjectMapperUtil.map(roleDto,Role.class);
        Role newRole = roleRepository.save(role);
        return ObjectMapperUtil.map(newRole,RoleDto.class);
    }

    @Override
    public RoleDto getRoleById(long id) {
        Role role = findRoleById(id);
        return ObjectMapperUtil.map(role,RoleDto.class);
    }

    @Override
    public RoleDto getRoleByName(String name) {
        Role role = findRoleByName(name);
        return ObjectMapperUtil.map(role,RoleDto.class);
    }

    @Override
    public RolesDto findRoles(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Role> roles = roleRepository.findAll(pageable);

        return new RolesDto(roles);
    }

    @Override
    public RoleDto updateRole(RoleDto roleDto, long id) {
        Role role = findRoleById(id);
        role.setName(roleDto.getName());
        Role updatedRole = roleRepository.save(role);
        return ObjectMapperUtil.map(updatedRole,RoleDto.class);
    }

    @Override
    public void deleteRole(long id) {
        Role role = findRoleById(id);
        roleRepository.delete(role);
    }

    private Role findRoleById(long id){
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Role.class, "id", id));
    }

    private Role findRoleByName(String name){
        return roleRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException(Role.class, "name", name));
    }
}
