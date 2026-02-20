package com.example.myhappyplants.controller;

import com.example.myhappyplants.service.SpeciesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/species")
public class SpeciesController {
    private final SpeciesService speciesService;


    public SpeciesController(SpeciesService speciesService) {this.speciesService = speciesService;}

    @GetMapping("/all")
    public ResponseEntity<?> getAllSpecies() {
        return ResponseEntity.status(HttpStatus.OK).body(speciesService.getAllSpecies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSpecies(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(speciesService.getSpecies(id));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchSpecies(@RequestParam String searchString) {
        return ResponseEntity.status(HttpStatus.OK).body(speciesService.getSpeciesSearched(searchString));
    }
}
