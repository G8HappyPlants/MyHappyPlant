package com.example.myhappyplants.entity;

import com.example.myhappyplants.auxillary.StringCryptograhicConverter;
import jakarta.persistence.*;

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
    @GeneratedValue (strategy = GenerationType.IDENTITY)
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

    protected User() {}

    public User(String username, String email, String emailHash, String passwordHash) {
        this.username = username;
        this.email = email;
        this.emailHash = emailHash;
        this.passwordHash = passwordHash;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
}
