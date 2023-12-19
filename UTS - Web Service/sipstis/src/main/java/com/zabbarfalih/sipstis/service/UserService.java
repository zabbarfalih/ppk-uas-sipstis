package com.zabbarfalih.sipstis.service;

import com.zabbarfalih.sipstis.dto.ProfileResponse;
import com.zabbarfalih.sipstis.dto.UserDto;
import com.zabbarfalih.sipstis.entity.User;

import java.util.List;

public interface UserService {
    User getUserLogged();
    ProfileResponse changeProfile(String name, String nip, String email, String noTelepon);
    ProfileResponse changePassword(String oldPassword, String newPassword);
    void deleteCurrentUser();


    UserDto createUser(UserDto user);
    UserDto createPBJUser(UserDto user);
    UserDto createPPKUser(UserDto user);
    UserDto createKepalaBauUser(UserDto user);

    List<UserDto> getAllUser();
    UserDto getUserById(Long id);
    UserDto getUserByEmail(String email);
    ProfileResponse getUserByNip(String nip);

}
