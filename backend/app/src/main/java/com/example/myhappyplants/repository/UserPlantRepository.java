package com.example.myhappyplants.repository;

import com.example.myhappyplants.entity.UserPlant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPlantRepository extends JpaRepository<UserPlant, Integer> {
    List<UserPlant> findAllByUserId(Long userId);

    Optional<UserPlant> findUserPlantByIdAndUserId(Integer id, Long userId);
}