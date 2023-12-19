package com.zabbarfalih.sipstis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
