package com.example.prac_sboot_security.sbootsecurity.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.EAN;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class LoginRequestDTO {
    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 6, max=40)
    private String password;
}
