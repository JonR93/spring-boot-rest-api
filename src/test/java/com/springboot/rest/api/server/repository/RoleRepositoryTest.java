package com.springboot.rest.api.server.repository;

import com.springboot.rest.api.server.entity.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private Role roleAdmin;
    private Role roleUser;

    @BeforeEach
    void setup(){
        roleAdmin = Role.builder()
                .name("ROLE_ADMIN")
                .build();

        roleUser = Role.builder()
                .name("ROLE_ADMIN")
                .build();
    }

    @Test
    void saveRole(){
        Role savedRole = roleRepository.save(roleAdmin);
        Assertions.assertThat(savedRole).isNotNull();
        Assertions.assertThat(savedRole.getId()).isPositive();
    }

    @Test
    void findByName() {
        roleRepository.save(roleAdmin);
        Role foundRole = roleRepository.findByName(roleAdmin.getName()).orElse(null);
        Assertions.assertThat(foundRole).isNotNull();
        Assertions.assertThat(foundRole.getName()).isEqualTo(roleAdmin.getName());
    }

    @Test
    void findAll(){
        roleRepository.save(roleAdmin);
        roleRepository.save(roleUser);
        List<Role> foundRoles = roleRepository.findAll();
        Assertions.assertThat(foundRoles).hasSize(2);
    }
}