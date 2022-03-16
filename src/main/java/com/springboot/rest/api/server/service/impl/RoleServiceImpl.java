package com.springboot.rest.api.server.service.impl;

import com.springboot.rest.api.server.entity.Role;
import com.springboot.rest.api.server.exception.ResourceNotFoundException;
import com.springboot.rest.api.server.payload.RoleDto;
import com.springboot.rest.api.server.payload.RolesDto;
import com.springboot.rest.api.server.repository.RoleRepository;
import com.springboot.rest.api.server.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    private ModelMapper mapper;

    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper mapper){
        this.roleRepository = roleRepository;
        this.mapper = mapper;
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        Role role = mapToEntity(roleDto);
        Role newRole = roleRepository.save(role);
        return mapToDTO(newRole);
    }

    @Override
    public RoleDto getRoleById(long id) {
        Role role = findRoleById(id);
        return mapToDTO(role);
    }

    @Override
    public RoleDto getRoleByName(String name) {
        Role role = findRoleByName(name);
        return mapToDTO(role);
    }

    @Override
    public RolesDto findRoles(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Role> users = roleRepository.findAll(pageable);

        // get content for page object
        List<Role> listOfRoles = users.getContent();

        List<RoleDto> content = listOfRoles.stream().map(this::mapToDTO).collect(Collectors.toList());

        RolesDto rolesResponse = new RolesDto();
        rolesResponse.setContent(content);
        rolesResponse.setPageNo(users.getNumber());
        rolesResponse.setPageSize(users.getSize());
        rolesResponse.setTotalElements(users.getTotalElements());
        rolesResponse.setTotalPages(users.getTotalPages());
        rolesResponse.setLast(users.isLast());

        return rolesResponse;
    }

    @Override
    public RoleDto updateRole(RoleDto roleDto, long id) {
        Role role = findRoleById(id);
        role.setName(roleDto.getName());
        Role updatedRole = roleRepository.save(role);
        return mapToDTO(updatedRole);
    }

    @Override
    public void deleteRole(long id) {
        Role role = findRoleById(id);
        roleRepository.delete(role);
    }

    private Role findRoleById(long id){
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
    }

    private Role findRoleByName(String name){
        return roleRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Role", "name", name));
    }

    // convert Entity into DTO
    private RoleDto mapToDTO(Role role){
        return mapper.map(role, RoleDto.class);
    }

    // convert DTO to entity
    private Role mapToEntity(RoleDto roleDto){
        return mapper.map(roleDto, Role.class);
    }
}
