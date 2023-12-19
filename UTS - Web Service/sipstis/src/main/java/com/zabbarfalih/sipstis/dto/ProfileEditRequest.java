package com.zabbarfalih.sipstis.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileEditRequest {
    @NotBlank(message = "Nama tidak boleh kosong")
    private String name;

    @NotBlank(message = "NIP tidak boleh kosong")
    private String nip;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Email harus valid")
    private String email;

    @NotBlank(message = "No Telepon tidak boleh kosong")
    private String noTelepon;
}
