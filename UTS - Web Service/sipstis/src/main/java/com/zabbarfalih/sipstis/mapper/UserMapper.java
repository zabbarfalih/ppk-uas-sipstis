package com.zabbarfalih.sipstis.mapper;

import com.zabbarfalih.sipstis.dto.ProfileResponse;
import com.zabbarfalih.sipstis.dto.UserDto;
import com.zabbarfalih.sipstis.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static User mapToUser(UserDto userDto){
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .nip(userDto.getNip())
                .email(userDto.getEmail())
                .noTelepon(userDto.getNoTelepon())
                .password(userDto.getPassword())
                .isDeleted(userDto.isDeleted())
                .build();
    }

    public static UserDto mapToUserDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .nip(user.getNip())
                .email(user.getEmail())
                .noTelepon(user.getNoTelepon())
                .password(user.getPassword())
                .isDeleted(user.getIsDeleted())
                .build();
    }

    public static ProfileResponse toProfileResponse(User user) {
        List<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        return ProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .nip(user.getNip())
                .email(user.getEmail())
                .noTelepon(user.getNoTelepon())
                .password(user.getPassword())
                .roles(roleNames)
                .isDeleted(user.getIsDeleted())
                .build();
    }
}
