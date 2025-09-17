package com.example.prac_sboot_security.sbootsecurity.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO {
    @Email
    @NotNull
    private String email;

    private int edad;

    @NotNull
    @Size(min=2, max=25)
    private String username;

}
