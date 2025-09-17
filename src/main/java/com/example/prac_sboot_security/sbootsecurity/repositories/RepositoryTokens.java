package com.example.prac_sboot_security.sbootsecurity.repositories;

import com.example.prac_sboot_security.sbootsecurity.models.entities.TokenEntity;
import com.example.prac_sboot_security.sbootsecurity.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface RepositoryTokens extends JpaRepository<TokenEntity, Long> {

    @Query("SELECT t FROM TokenEntity t WHERE t.expired = false OR t.revoked = false")
    List<TokenEntity> obtenerTokensSinRevocarOExpirar();

    @Modifying
    @Transactional
    @Query("UPDATE TokenEntity t SET t.revoked = true, t.expired = true WHERE t.userEntity =:userID")
    void revocarOExpirarTokens(@Param("userID") UserEntity userID);
}
