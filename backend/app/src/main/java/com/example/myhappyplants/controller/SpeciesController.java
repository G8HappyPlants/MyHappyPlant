package com.example.myhappyplants.controller;

import com.example.myhappyplants.service.SpeciesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/species")
@RequiredArgsConstructor
public class SpeciesController {
    private final SpeciesService speciesService;

    @GetMapping
    public ResponseEntity<?> getAllSpecies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String query) {
        if (query.isBlank())
            return ResponseEntity.ok(speciesService.getAllSpecies(page));
        else
            return ResponseEntity.ok(speciesService.getSpeciesSearched(query, page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSpecies(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(speciesService.getSpecies(id));
    }
}
