package com.example.prac_sboot_security.sbootsecurity.controllers;

import com.example.prac_sboot_security.sbootsecurity.models.dtos.UserDTO;
import com.example.prac_sboot_security.sbootsecurity.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserDTO>> getUsers(@RequestHeader(HttpHeaders.AUTHORIZATION) String authToken){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.ACCEPTED);
    }
}
