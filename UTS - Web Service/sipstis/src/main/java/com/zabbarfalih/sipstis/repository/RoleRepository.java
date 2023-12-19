package com.zabbarfalih.sipstis.repository;

import com.zabbarfalih.sipstis.entity.RoleEnum;
import com.zabbarfalih.sipstis.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}