package com.zabbarfalih.sipstis.repository;

import com.zabbarfalih.sipstis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByEmail(String email);
    User findByNip(String nip);
    boolean existsByEmail(String email);
    boolean existsByNip(String nip);
    List<User> findAll();
    List<User> searchByNameContaining(String name);
    List<User> searchByEmailContaining(String email);
    List<User> searchByNipContaining(String nip);
    List<User> searchByNipContainingOrNameContainingOrEmailContaining(String nip, String name, String email);
}