package com.example.myhappyplants.dto;

import com.example.myhappyplants.entity.User;
import com.example.myhappyplants.entity.UserPlant;

import java.time.LocalDate;

public record CreateUserPlantRequest(
        Integer id,
        String nickname,
        LocalDate lastWatered,
        Integer waterFrequency,
        Long trefleId) {

    public static CreateUserPlantRequest fromUserPlant(UserPlant userPlant) {
        return new CreateUserPlantRequest(
                userPlant.getId(),
                userPlant.getNickname(),
                userPlant.getLastWatered(),
                userPlant.getWaterFrequency(),
                userPlant.getTrefleId().getTrefleId()
        );
    }
}
