package com.example.myhappyplants.entity;

import com.example.myhappyplants.auxillary.StringCryptograhicConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "username")
        }
)

public class User {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(name = "username", nullable = false)
    private String username;

    @Setter
    @Getter
    @Convert(converter = StringCryptograhicConverter.class)
    @Column(name = "email", nullable = false)
    private String email;

    @Setter
    @Getter
    @Column(name = "email_hash", nullable = false)
    private String emailHash;

    @Setter
    @Getter
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Setter
    @Getter
    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;

    @Setter
    @Getter
    @Column(name = "verification_token")
    private String verificationToken;

    @Setter
    @Getter
    @Column(name = "verification_expires_at")
    private Instant verificationExpiresAt;

    @Setter
    @Getter
    @Column(name = "last_notification_send_at")
    private Instant lastNotificationSendAt;

    protected User() {
    }

    public User(String username, String email, String emailHash, String passwordHash) {
        this.username = username;
        this.email = email;
        this.emailHash = emailHash;
        this.passwordHash = passwordHash;
    }

}
