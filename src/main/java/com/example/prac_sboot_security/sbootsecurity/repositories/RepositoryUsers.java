package com.example.prac_sboot_security.sbootsecurity.repositories;

import com.example.prac_sboot_security.sbootsecurity.models.entities.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositoryUsers extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByEmail(String email);
}
