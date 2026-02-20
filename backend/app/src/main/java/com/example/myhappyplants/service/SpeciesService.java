package com.example.myhappyplants.service;

import com.example.myhappyplants.dto.SpeciesDTO;
import com.example.myhappyplants.repository.SpeciesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpeciesService {
    private final SpeciesRepository speciesRepository;

    public SpeciesService(SpeciesRepository speciesRepository) {this.speciesRepository = speciesRepository;}

    public ResponseEntity<List<SpeciesDTO>> getAllSpecies() {
        List<SpeciesDTO> speciesList = speciesRepository.findAll()
                .stream()
                //.filter() //TODO - hitta ett sätt att inte hämta 10,000+ växter i ett svep
                .map(species -> new SpeciesDTO(species))
                .limit(30) //TODO - TEMP - gör att en bara får 30 resultat för tillfället.
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(speciesList);
    }
}
