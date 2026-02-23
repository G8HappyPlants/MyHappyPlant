package com.example.myhappyplants.service;

import com.example.myhappyplants.dto.SpeciesResponse;
import com.example.myhappyplants.repository.SpeciesRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpeciesService {
    private final SpeciesRepository speciesRepository;

    public SpeciesService(SpeciesRepository speciesRepository) {this.speciesRepository = speciesRepository;}

    public ResponseEntity<List<SpeciesResponse>> getAllSpecies(int pageNumber) {
        List<SpeciesResponse> speciesList = speciesRepository.findAll(
                Pageable.ofSize(30).withPage(pageNumber)
                )
                .stream()
                .map(SpeciesResponse::fromSpecies)
                .toList();

        return ResponseEntity.ok(speciesList);
    }

    public ResponseEntity<SpeciesResponse> getSpecies(String id) {
        return speciesRepository.findById(Long.valueOf(id))
                .map(species -> ResponseEntity.status(HttpStatus.OK).body(SpeciesResponse.fromSpecies(species)))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<SpeciesResponse>> getSpeciesSearched(String searchString) {
        return null; //TODO - Whatever the list the searchfunction finds somehow.
    }
}
