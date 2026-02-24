package com.example.myhappyplants.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "user_plants")
public class UserPlant {
    //region columns
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // int i DB

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // FK
    private User user;

    @Getter
    @Setter
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Getter
    @Setter
    @Column(name = "plant_desc")
    private String plantDescription;

    @Getter
    @Setter
    @Column(name = "last_watered", nullable = false)
    private Instant lastWatered; // date i DB

    @Getter
    @Setter
    @Column(name = "water_frequency")
    private Integer waterFrequency;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trefle_id")
    private Species linkedSpecies;

    @Getter
    @Setter
    @Column(name = "image_url")
    private String imageUrl; // char(255) i DB

    @Getter
    @Setter
    @CreationTimestamp
    @Column(name = "added_at")
    private Instant addedAt; // datetime i DB (nullable)
    //endregion columns

    public UserPlant() {
    }
}