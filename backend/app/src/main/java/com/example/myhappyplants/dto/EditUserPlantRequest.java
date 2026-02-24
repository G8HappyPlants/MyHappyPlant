package com.example.myhappyplants.dto;

import com.example.myhappyplants.entity.UserPlant;

import java.time.LocalDate;

public record EditUserPlantRequest(
        Integer id,
        String nickname,
        LocalDate lastWatered,
        Integer waterFrequency,
        Long trefleId) {
}
