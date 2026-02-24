package com.example.myhappyplants.dto;

import com.example.myhappyplants.entity.UserPlant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;

public record CreateUserPlantRequest(
        @NotBlank String nickname,
        String description,
        @NotNull Instant lastWatered,
        @NotNull Integer waterFrequency,
        @NotNull Long trefleId) {
}
