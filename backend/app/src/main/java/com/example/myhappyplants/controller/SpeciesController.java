package com.example.myhappyplants.controller;

import ch.qos.logback.core.util.StringUtil;
import com.example.myhappyplants.service.SpeciesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Pattern;


@RestController
@RequestMapping("/api/species")
public class SpeciesController {
    private final SpeciesService speciesService;


    public SpeciesController(SpeciesService speciesService) {this.speciesService = speciesService;}

    @GetMapping
    public ResponseEntity<?> getAllSpecies(@RequestParam(defaultValue = "0") String page) {
        if (Pattern.matches("^-?\\d+$", page))
            return ResponseEntity.status(HttpStatus.OK).body(speciesService.getAllSpecies(Integer.parseInt(page)));
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Pages parameter must be an integer");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSpecies(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(speciesService.getSpecies(id));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchSpecies(@RequestParam String searchString, @RequestParam(defaultValue = "30") Integer limit) {
        return ResponseEntity.status(HttpStatus.OK).body(speciesService.getSpeciesSearched(searchString, limit));
    }
}
