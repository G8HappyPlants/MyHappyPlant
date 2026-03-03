package com.example.myhappyplants.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record EditUserPlantRequest(
        @NotBlank String nickname,
        String description,
        @NotNull Instant lastWatered,
        @NotNull Integer waterFrequency) {
}
