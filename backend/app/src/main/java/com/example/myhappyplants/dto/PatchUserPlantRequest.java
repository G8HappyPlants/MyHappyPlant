package com.example.myhappyplants.dto;

import java.time.Instant;
import java.util.List;

public record PatchUserPlantRequest(
        String nickname,
        String description,
        Instant lastWatered,
        Integer waterFrequency,
        List<String> tagNames,
        String imageBase64,
        String imageContentType) {
}
