package com.example.myhappyplants.dto;

import java.time.Instant;

public record PatchUserPlantRequest(
        String nickname,
        String description,
        Instant lastWatered,
        Integer waterFrequency) {
}
