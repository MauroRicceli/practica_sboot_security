package com.example.prac_sboot_security.sbootsecurity.services;

import com.example.prac_sboot_security.sbootsecurity.models.dtos.UserDTO;
import com.example.prac_sboot_security.sbootsecurity.models.entities.UserEntity;
import com.example.prac_sboot_security.sbootsecurity.repositories.RepositoryUsers;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RepositoryUsers repositoryUsers;

    public List<UserDTO> getUsers(){
        List<UserEntity> usuarios = repositoryUsers.findAll();
        List<UserDTO> response = new ArrayList<UserDTO>();
        //esto deberia hacerlo mapStruct pero para no importar cosas adicionales lo hago manualmente
        for(UserEntity aux : usuarios){
            response.add(UserDTO.builder().edad(aux.getEdad()).email(aux.getEmail()).username(aux.getUsername()).build());
        }
        return response;
    }
}
