package com.example.prac_sboot_security.sbootsecurity.services;

import com.example.prac_sboot_security.sbootsecurity.models.entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

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

    //CLAIMS INFORMACION QUE ME SIRVA ADICIONAL, COMO EL ROL.
    //SUBJECT EL IDENTIFICADOR QUE ESTE USANDO, POR EJEMPLO AHORA EMAIL. LA CONVENCION ES DECIRLE USERNAME. !!!!
    private String buildToken(UserEntity user, long expiration){
        return Jwts.builder()
                .id(UUID.randomUUID().toString()) //id del token generada random
                .claims(Map.of("name", user.getUsername())) //opcional
                .subject(user.getEmail()) //como voy a identificarlo al dueño
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String refreshToken){
        Claims jstToken = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload();

        return jstToken.getSubject();
    }

    //verifico que el token pertenezca a ese usuario extraido y que no esté expirado
    public boolean isTokenValid(String token, UserEntity user){
        String username = extractUsername(token);
        return username.equals(user.getEmail()) && !isTokenExpired(token);

    }

    //token refresh expiration y fecha actual.
    private boolean isTokenExpired(String token){
       return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        Claims tkn = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return tkn.getExpiration();
    }

    //crear mi SecretKey para realizar la firma de los token
    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
