package com.example.myhappyplants.dto;

import com.example.myhappyplants.entity.User;
import com.example.myhappyplants.entity.UserPlant;

import java.time.LocalDate;

public record EditUserPlantRequest(
        Integer id,
        String nickname,
        LocalDate lastWatered,
        Integer waterFrequency,
        Long trefleId) {

    public static EditUserPlantRequest fromUserPlant(UserPlant userPlant) {
        return new EditUserPlantRequest(
                userPlant.getId(),
                userPlant.getNickname(),
                userPlant.getLastWatered(),
                userPlant.getWaterFrequency(),
                userPlant.getTrefleId().getTrefleId()
        );
    }

    public UserPlant toUserPlant(User userContext) {
        return null;
    }
}
