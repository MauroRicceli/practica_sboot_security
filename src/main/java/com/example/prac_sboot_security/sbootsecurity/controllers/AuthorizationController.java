package com.example.prac_sboot_security.sbootsecurity.controllers;

import com.example.prac_sboot_security.sbootsecurity.models.dtos.RegisterRequestDTO;
import com.example.prac_sboot_security.sbootsecurity.models.dtos.TokenResponseDTO;
import com.example.prac_sboot_security.sbootsecurity.services.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<TokenResponseDTO> registerUser(@RequestBody RegisterRequestDTO userRegister){
        System.out.println("XDD");
        return new ResponseEntity<>(authorizationService.register(userRegister), HttpStatus.ACCEPTED);
    }
}
