package com.example.myhappyplants.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_plants")
public class UserPlant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // int i DB


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // FK
    private User user;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "last_watered", nullable = false)
    private LocalDate lastWatered; // date i DB

    @Column(name ="water_frequency") private Integer waterFrequency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trefle_id")
    private Species trefleId;

    @Column(name = "plant_id", nullable = false)
    private String plantId; // char(255) i DB

    @Column(name = "image_url", nullable = false)
    private String imageUrl; // char(255) i DB

    @Column(name = "added_at")
    private LocalDateTime addedAt; // datetime i DB (nullable)


    protected UserPlant() {}


    public UserPlant(User user, String nickname, String plantId, String imageUrl, LocalDate lastWatered) {
        this.user = user;
        this.nickname = nickname;
        this.plantId = plantId;
        this.imageUrl = imageUrl;
        this.lastWatered = lastWatered;
        this.addedAt = LocalDateTime.now();
    }

    // Getters
    public Integer getId() { return id; }
    public User getUser() { return user; }
    public String getNickname() { return nickname; }
    public LocalDate getLastWatered() { return lastWatered; }
    public String getPlantId() { return plantId; }
    public String getImageUrl() { return imageUrl; }
    public LocalDateTime getAddedAt() { return addedAt; }

    // Setters
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setLastWatered(LocalDate lastWatered) { this.lastWatered = lastWatered; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getWaterFrequency() {
        return waterFrequency;
    }

    public void setWaterFrequency(Integer waterFrequency) {
        this.waterFrequency = waterFrequency;
    }

    public Species getTrefleId() {
        return trefleId;
    }

    public void setTrefleId(Species trefleId) {
        this.trefleId = trefleId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}