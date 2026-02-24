package com.example.myhappyplants.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record PatchUserPlantRequest(
        String nickname,
        String description,
        Instant lastWatered,
        Integer waterFrequency) {
}
