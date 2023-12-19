package com.zabbarfalih.sipstis.service;

import com.zabbarfalih.sipstis.dto.ProfileResponse;
import com.zabbarfalih.sipstis.dto.UserDto;
import com.zabbarfalih.sipstis.entity.RoleEnum;
import com.zabbarfalih.sipstis.entity.Role;
import com.zabbarfalih.sipstis.entity.User;
import com.zabbarfalih.sipstis.exception.UnauthorizedException;
import com.zabbarfalih.sipstis.exception.UserNotFoundException;
import com.zabbarfalih.sipstis.mapper.UserMapper;
import com.zabbarfalih.sipstis.repository.RoleRepository;
import com.zabbarfalih.sipstis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserLogged() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userRepository.findByEmail(currentPrincipalName);
    }

    @Override
    public ProfileResponse changeProfile(String name, String nip ,String email, String noTelepon) {
        if (name == null ||
                nip == null ||
                email == null ||
                noTelepon == null) {
            throw new IllegalArgumentException("Semua Isian Harus Diisi");
        }

        User user = getUserLogged();
        user.setName(name);
        user.setNip(nip);
        user.setEmail(email);
        user.setNoTelepon(noTelepon);

        userRepository.save(user);
        return UserMapper.toProfileResponse(user);
    }

    @Override
    public ProfileResponse changePassword(String oldPassword, String newPassword) {
        User user = getUserLogged();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UnauthorizedException("Error: Password lama tidak sesuai");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return UserMapper.toProfileResponse(user);
    }

    @Override
    public void deleteCurrentUser() {
        User user = getUserLogged();
        if (user != null) {
            user.setIsDeleted(true);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("User tidak ditemukan atau sudah dihapus");
        }
    }


    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByNip(userDto.getNip()) && userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Error: NIP dan Email Sudah Terdaftar. Tidak Bisa Mendaftar Dengan NIP dan Email Sama!");
        }
        else if (userRepository.existsByNip(userDto.getNip())) {
            throw new RuntimeException("Error: NIP Sudah Terdaftar. Tidak Bisa Mendaftar Dengan NIP Sama!");
        }
        else if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Error: Email Sudah Terdaftar. Tidak Bisa Mendaftar Dengan Email Sama!");
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(RoleEnum.ROLE_UNIT).orElseThrow(() -> new RuntimeException("Role dengan nama ROLE_UNIT tidak ditemukan"));
        roles.add(userRole);
        User user = UserMapper.mapToUser(userDto);
        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    public UserDto createPBJUser(UserDto userDto) {
        if (userRepository.existsByNip(userDto.getNip()) && userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Error: NIP dan Email Sudah Terdaftar. Tidak Bisa Mendaftar Dengan NIP dan Email Sama!");
        }
        else if (userRepository.existsByNip(userDto.getNip())) {
            throw new RuntimeException("Error: NIP Sudah Terdaftar. Tidak Bisa Mendaftar Dengan NIP Sama!");
        }
        else if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Error: Email Sudah Terdaftar. Tidak Bisa Mendaftar Dengan Email Sama!");
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(RoleEnum.ROLE_PBJ).orElseThrow(() -> new RuntimeException("Role dengan nama ROLE_PBJ tidak ditemukan"));
        roles.add(userRole);
        User user = UserMapper.mapToUser(userDto);
        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    public UserDto createPPKUser(UserDto userDto) {
        if (userRepository.existsByNip(userDto.getNip()) && userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Error: NIP dan Email Sudah Terdaftar. Tidak Bisa Mendaftar Dengan NIP dan Email Sama!");
        }
        else if (userRepository.existsByNip(userDto.getNip())) {
            throw new RuntimeException("Error: NIP Sudah Terdaftar. Tidak Bisa Mendaftar Dengan NIP Sama!");
        }
        else if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Error: Email Sudah Terdaftar. Tidak Bisa Mendaftar Dengan Email Sama!");
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(RoleEnum.ROLE_PPK).orElseThrow(() -> new RuntimeException("Role dengan nama ROLE_PPK tidak ditemukan"));
        roles.add(userRole);
        User user = UserMapper.mapToUser(userDto);
        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    public UserDto createKepalaBauUser(UserDto userDto) {
        if (userRepository.existsByNip(userDto.getNip()) && userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Error: NIP dan Email Sudah Terdaftar. Tidak Bisa Mendaftar Dengan NIP dan Email Sama!");
        }
        else if (userRepository.existsByNip(userDto.getNip())) {
            throw new RuntimeException("Error: NIP Sudah Terdaftar. Tidak Bisa Mendaftar Dengan NIP Sama!");
        }
        else if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Error: Email Sudah Terdaftar. Tidak Bisa Mendaftar Dengan Email Sama!");
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(RoleEnum.ROLE_KEPALA_BAU).orElseThrow(() -> new RuntimeException("Role dengan nama ROLE_KEPALA_BAU tidak ditemukan"));
        roles.add(userRole);
        User user = UserMapper.mapToUser(userDto);
        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    public List<UserDto> getAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
                    .map(UserMapper::mapToUserDto)
                    .collect(Collectors.toList());
    }
    
    @Override
    public UserDto getUserById(Long id) throws RuntimeException {
        User user = userRepository.findById(id).get();
        if (user == null) {
            throw new RuntimeException("User dengan ID " + '"' + id + '"' + " tidak ditemukan");
        }
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) throws RuntimeException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User dengan email " + '"' + email + '"' + " tidak ditemukan");
        }
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public ProfileResponse getUserByNip(String nip) throws RuntimeException {
        User user = userRepository.findByNip(nip);
        if (user == null) {
            throw new RuntimeException("User dengan NIP " + '"' + nip + '"' + " tidak ditemukan");
        }
        return UserMapper.toProfileResponse(user);
    }
}
