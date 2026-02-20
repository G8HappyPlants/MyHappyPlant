package com.example.myhappyplants.controller;

import com.example.myhappyplants.config.SecurityConfig;
import com.example.myhappyplants.dto.SpeciesDTO;
import com.example.myhappyplants.service.SpeciesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/species")
public class SpeciesController {
    private final SpeciesService speciesService;

    public SpeciesController(SpeciesService speciesService) {this.speciesService = speciesService;}

    @GetMapping("/all")
    public ResponseEntity<?> getAllSpecies() {
        return ResponseEntity.status(HttpStatus.OK).body(speciesService.getAllSpecies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpeciesDTO> getSpecies(@PathVariable String id) {
        return null;
    }

    @GetMapping("/search")
    public ResponseEntity<List<SpeciesDTO>> searchSpecies(@RequestParam String searchString) {
        return null;
    }
}
