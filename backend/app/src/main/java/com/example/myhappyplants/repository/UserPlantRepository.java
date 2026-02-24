package com.example.myhappyplants.repository;

import com.example.myhappyplants.entity.UserPlant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPlantRepository extends JpaRepository<UserPlant, Integer> {
    List<UserPlant> findAllByUserId(Long userId);

    List<UserPlant> findAllByUserId(Long userId, Pageable pageable);

    Optional<UserPlant> findUserPlantByIdAndUserId(Integer id, Long userId);
}