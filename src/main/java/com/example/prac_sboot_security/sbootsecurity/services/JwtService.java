package com.example.prac_sboot_security.sbootsecurity.services;

import com.example.prac_sboot_security.sbootsecurity.models.entities.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long jwtRefreshExpiration;

    public String generateToken(UserEntity user){
        return buildToken(user, jwtExpiration);
    }

    public String generateRefreshToken(UserEntity user){
        return buildToken(user, jwtRefreshExpiration);
    }

    private String buildToken(UserEntity user, long expiration){
        return Jwts.builder()
                .id(user.getId().toString()) //id del token
                .claims(Map.of("name", user.getUsername())) //opcional
                .subject(user.getEmail()) //como voy a identificarlo al dueño
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .compact();
    }

    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
