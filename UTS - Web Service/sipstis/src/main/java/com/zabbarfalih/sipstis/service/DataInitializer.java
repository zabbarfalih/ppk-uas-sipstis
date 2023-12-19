package com.zabbarfalih.sipstis.service;

import com.zabbarfalih.sipstis.entity.Role;
import com.zabbarfalih.sipstis.entity.RoleEnum;
import com.zabbarfalih.sipstis.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Bean
    public CommandLineRunner initializeData() {
        return args -> {
            for (RoleEnum roleName : RoleEnum.values()) {
                if (roleRepository.findByName(roleName).isEmpty()) {
                    Role role = new Role();
                    role.setName(roleName);
                    roleRepository.save(role);
                }
            }
        };
    }
}