package com.example.myhappyplants.controller;

import com.example.myhappyplants.dto.SpeciesDTO;
import com.example.myhappyplants.service.SpeciesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/species")
public class SpeciesController {
    private final SpeciesService speciesService;

    public SpeciesController(SpeciesService speciesService) {this.speciesService = speciesService;}

    @GetMapping("/all")
    public ResponseEntity<SpeciesDTO> getAllSpecies() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpeciesDTO> getSpecies(@PathVariable String id) {
        return null;
    }
}
