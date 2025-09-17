package com.example.prac_sboot_security.sbootsecurity.models.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterRequestDTO {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 6, max=40)
    private String password;

    @Size(min=2, max=25)
    @NotNull
    private String username;

}
