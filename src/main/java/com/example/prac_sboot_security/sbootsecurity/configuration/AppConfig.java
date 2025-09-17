package com.example.prac_sboot_security.sbootsecurity.configuration;

import com.example.prac_sboot_security.sbootsecurity.models.entities.TokenEntity;
import com.example.prac_sboot_security.sbootsecurity.models.entities.UserEntity;
import com.example.prac_sboot_security.sbootsecurity.repositories.RepositoryTokens;
import com.example.prac_sboot_security.sbootsecurity.repositories.RepositoryUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@RequiredArgsConstructor
@Configuration
public class AppConfig {

    private final RepositoryUsers repositoryUsers;
    private final RepositoryTokens repositoryTokens;

    @Bean
    public PasswordEncoder codificarPassword(){
        return new BCryptPasswordEncoder();
    }


    //CUALQUIER USUARIO NO AUTENTICADO PUEDE ACCEDER A LOS ENDPOINTS DE AUTH.
    //SI NO LO ESTA, SE VERIFICAN LOS FILTROS Y SI ES VALIDO SE CREA EL USERAUTHENTICATIONTOKEN, PARA QUE SBOOT SEPA SI PUEDE ACCEDER O NO AL ENDPOINT.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                ).sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/api/auth/logout")
                                                                .addLogoutHandler((request, response, authentication) -> {
                                                                    final var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                                                                    logout(authHeader);
                                                                })
                                                                .logoutSuccessHandler((request, response, authentication) ->
                                                                        SecurityContextHolder.clearContext())
                );


        return http.build();
    }

    //SI SE DESLOGEA, REVOCO SU TOKEN EN LA DB.
    private void logout(final String token){
        if(token == null || !token.startsWith("Bearer ")){
            throw new IllegalArgumentException("Invalid token");
        }
        final String tkn = token.substring(7);
        final TokenEntity foundToken = repositoryTokens.findByToken(tkn).orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        repositoryTokens.revocarOExpirarToken(tkn);

    }

    //autenticacion metodo sboot
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            final UserEntity user = repositoryUsers.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return org.springframework.security.core.userdetails.User
                    .builder()
                    .username(user.getEmail())
                    .password(user.getHashedPw())
                    .build();
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(codificarPassword());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
