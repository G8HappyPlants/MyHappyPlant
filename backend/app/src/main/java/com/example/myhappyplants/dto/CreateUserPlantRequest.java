package com.example.myhappyplants.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

public record CreateUserPlantRequest(
        @NotBlank String nickname,
        String description,
        @NotNull Instant lastWatered,
        @NotNull Integer waterFrequency,
        @NotNull Long trefleId,
        String imageBase64,
        String imageContentType,
        List<String> tagNames) {
}
