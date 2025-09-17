package com.example.prac_sboot_security.sbootsecurity.models.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@Entity
@Table(name="usuarios")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Email
    @NotNull
    private String email;

    @Column(nullable = false)
    @NotNull
    @Size(min = 6)
    private String hashedPw;

    @Column(unique = true)
    @Size(min=2, max=25)
    @NotNull
    private String username;

    private int edad;

}
