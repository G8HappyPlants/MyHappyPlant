package com.example.myhappyplants.entity;

import com.example.myhappyplants.auxillary.StringCryptograhicConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "username")
        }
)

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Convert(converter = StringCryptograhicConverter.class)
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "email_hash", nullable = false)
    private String emailHash;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Setter
    @Getter
    @Column(nullable = false)
    private boolean emailVerified = false;

    @Setter
    @Getter
    @Column
    private String verificationToken;

    @Setter
    @Getter
    @Column
    private Instant verificationExpiresAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPlant> userPlants = new ArrayList<>();

    protected User() {
    }

    public User(String username, String email, String emailHash, String passwordHash) {
        this.username = username;
        this.email = email;
        this.emailHash = emailHash;
        this.passwordHash = passwordHash;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public List<UserPlant> getUserPlants() {
        return userPlants;
    }
}
