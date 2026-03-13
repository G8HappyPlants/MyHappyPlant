package com.example.myhappyplants.dto;

import com.example.myhappyplants.entity.UserPlant;

import java.time.Instant;
import java.util.List;

public record UserPlantResponse(
        Integer id,
        String nickname,
        String description,
        Instant lastWatered,
        Integer waterFrequency,
        Long speciesId,
        String imageUrl,
        Instant addedAt,
        List<TagResponse> tags
) {

    public static UserPlantResponse fromUserPlant(UserPlant plant) {
        return new UserPlantResponse(
                plant.getId(),
                plant.getNickname(),
                plant.getPlantDescription(),
                plant.getLastWatered(),
                plant.getWaterFrequency(),
                plant.getLinkedSpecies().getTrefleId(),
                plant.getImageUrl(),
                plant.getAddedAt(),
                plant.getTags().stream().map(TagResponse::fromTag).toList()
        );
    }
}
