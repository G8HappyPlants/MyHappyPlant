package com.example.myhappyplants.repository;

import com.example.myhappyplants.entity.Species;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeciesRepository extends JpaRepository<Species, Long> {
    @Query("SELECT MAX(trefleId) FROM Species")
    long getTopTrefleId();

    List<Species> findByCommonNameContainingIgnoreCase(String commonName, Limit pageable);
}
