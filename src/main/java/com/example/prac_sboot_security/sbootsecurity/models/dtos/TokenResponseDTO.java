package com.example.prac_sboot_security.sbootsecurity.models.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TokenResponseDTO {

    @NotNull
    private String access_token;
    private String refresh_token;
}
