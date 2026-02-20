package com.example.myhappyplants.dto;

import com.example.myhappyplants.entity.UserPlant;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserPlantDTO {
    private Integer id;
    private Long user_id;
    private String nickname;
    private LocalDate lastWatered;
    private Integer waterFrequency;
    private Long speciesId;
    private LocalDateTime createdAt;

    public UserPlantDTO(UserPlant userPlant){
        this.id = userPlant.getId();
        this.user_id = userPlant.getUser().getId();
        this.nickname = userPlant.getNickname();
        this.lastWatered = userPlant.getLastWatered();
        this.waterFrequency = userPlant.getWaterFrequency();
        this.speciesId = userPlant.getTrefleId().getTrefleId();
        this.createdAt = userPlant.getAddedAt();
    }

    //TODO - Missing get user and get species from their ID to convert properly. 
    public UserPlant toUserPlant(){
        return null;
    }
}
