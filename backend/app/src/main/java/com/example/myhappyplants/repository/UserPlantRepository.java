package com.example.myhappyplants.repository;

import com.example.myhappyplants.entity.UserPlant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserPlantRepository extends JpaRepository<UserPlant, Integer> {

    List<UserPlant> findAllByUserId(Long userId);
}