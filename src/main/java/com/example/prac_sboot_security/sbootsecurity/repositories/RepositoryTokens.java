package com.example.prac_sboot_security.sbootsecurity.repositories;

import com.example.prac_sboot_security.sbootsecurity.models.entities.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryTokens extends JpaRepository<TokenEntity, Long> {
}
