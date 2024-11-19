package com.effictivemobile.taskmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email")
    @Email(message = "Email should be valid")
    @NotNull(message = "Email cannot be null")
    private String email;
    @Column(name = "password")
    @Size(min = 6, message = "Password should be at least 6 characters long")
    private String password;
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Role> roles;
}
