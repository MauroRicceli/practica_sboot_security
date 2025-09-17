package com.example.prac_sboot_security.sbootsecurity.services;

import com.example.prac_sboot_security.sbootsecurity.models.dtos.RegisterRequestDTO;
import com.example.prac_sboot_security.sbootsecurity.models.dtos.TokenResponseDTO;
import com.example.prac_sboot_security.sbootsecurity.models.entities.TokenEntity;
import com.example.prac_sboot_security.sbootsecurity.models.entities.UserEntity;
import com.example.prac_sboot_security.sbootsecurity.repositories.RepositoryTokens;
import com.example.prac_sboot_security.sbootsecurity.repositories.RepositoryUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthorizationService {

    private final PasswordEncoder passwordEncoder;
    private final RepositoryUsers repositoryUsers;
    private final RepositoryTokens repositoryTokens;
    private final JwtService jwtService;


    public TokenResponseDTO register(RegisterRequestDTO request){

        var user = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .hashedPw(passwordEncoder.encode(request.getPassword()))
                .build();

        System.out.println(user);

        repositoryUsers.save(user);

        var tkn = TokenEntity.builder()
                .token(jwtService.generateToken(user))
                .tokenType(TokenEntity.TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .userEntity(user)
                .build();

        repositoryTokens.save(tkn);

        return new TokenResponseDTO(jwtService.generateToken(user), jwtService.generateRefreshToken(user));


    }
}
