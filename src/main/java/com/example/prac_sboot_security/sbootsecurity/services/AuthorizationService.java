package com.example.prac_sboot_security.sbootsecurity.services;

import com.example.prac_sboot_security.sbootsecurity.models.dtos.LoginRequestDTO;
import com.example.prac_sboot_security.sbootsecurity.models.dtos.RegisterRequestDTO;
import com.example.prac_sboot_security.sbootsecurity.models.dtos.TokenResponseDTO;
import com.example.prac_sboot_security.sbootsecurity.models.entities.TokenEntity;
import com.example.prac_sboot_security.sbootsecurity.models.entities.UserEntity;
import com.example.prac_sboot_security.sbootsecurity.repositories.RepositoryTokens;
import com.example.prac_sboot_security.sbootsecurity.repositories.RepositoryUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthorizationService {

    private final PasswordEncoder passwordEncoder;
    private final RepositoryUsers repositoryUsers;
    private final RepositoryTokens repositoryTokens;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public TokenResponseDTO register(RegisterRequestDTO request){

        var user = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .hashedPw(passwordEncoder.encode(request.getPassword()))
                .build();

        repositoryUsers.save(user);

        String tknExpiration = jwtService.generateToken(user);
        String tknExpirationRefresh = jwtService.generateRefreshToken(user);

        var tkn = TokenEntity.builder()
                .token(tknExpiration)
                .tokenType(TokenEntity.TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .userEntity(user)
                .build();

        repositoryTokens.save(tkn);

        return new TokenResponseDTO(tknExpiration,tknExpirationRefresh);


    }

    public TokenResponseDTO login(LoginRequestDTO loginRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        UserEntity user = repositoryUsers.findByEmail(loginRequest.getEmail()).orElseThrow();

        repositoryTokens.revocarOExpirarTokensUsuario(user);

        repositoryTokens.save(TokenEntity.builder()
                .token(jwtService.generateToken(user))
                .tokenType(TokenEntity.TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .userEntity(user)
                .build());

        return new TokenResponseDTO(jwtService.generateToken(user), jwtService.generateRefreshToken(user));

    }

    private void revocarTokensUsuario(UserEntity user){
        repositoryTokens.revocarOExpirarTokensUsuario(user);
    }

    public TokenResponseDTO refrescarToken(String authHeader){
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new IllegalArgumentException("Invalid Bearer Token");
        }

        String refreshToken = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(refreshToken);

        if(userEmail == null){
            throw new IllegalArgumentException("Invalid refresh token, email null");
        }

        UserEntity user = repositoryUsers.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(userEmail));

        if(!jwtService.isTokenValid(refreshToken, user)){
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String tkn = jwtService.generateToken(user);
        String refTkn = jwtService.generateRefreshToken(user);

        repositoryTokens.revocarOExpirarTokensUsuario(user);
        repositoryTokens.save(TokenEntity.builder()
                .tokenType(TokenEntity.TokenType.BEARER)
                .token(tkn)
                .userEntity(user)
                .revoked(false)
                .expired(false)
                .build());

        return new TokenResponseDTO(tkn, refTkn);
    }
}
