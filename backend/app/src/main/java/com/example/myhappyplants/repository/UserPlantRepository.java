package com.example.myhappyplants.repository;

import com.example.myhappyplants.entity.User;
import com.example.myhappyplants.entity.UserPlant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserPlantRepository extends JpaRepository<UserPlant, Integer> {
    List<UserPlant> findAllByUser(User user, Pageable pageable);

    Optional<UserPlant> findUserPlantByUserAndId(User user, Integer id);

    List<UserPlant> findAllByUserAndNextWaterDateLessThanEqual(User user, LocalDate date);
}