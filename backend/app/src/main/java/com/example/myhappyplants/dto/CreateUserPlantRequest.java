package com.example.myhappyplants.dto;

import com.example.myhappyplants.entity.UserPlant;

import java.time.Instant;
import java.time.LocalDate;

public record CreateUserPlantRequest(
        Integer id,
        String nickname,
        String description,
        Instant lastWatered,
        Integer waterFrequency,
        Long trefleId) {
}
