package com.example.myhappyplants.controller;

import com.example.myhappyplants.dto.OwnedPlantDTO;
import com.example.myhappyplants.service.OwnedPlantsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owned")
public class OwnedPlantsController {
    private final OwnedPlantsService OwnedLibraryService;

    public OwnedPlantsController(OwnedPlantsService OwnedLibraryService) {this.OwnedLibraryService = OwnedLibraryService;}

    @GetMapping("/all")
    public ResponseEntity<OwnedPlantDTO> getAllOwnedLibrary() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnedPlantDTO> getOwnedLibrary(@PathVariable String id) {
        return null;
    }

    @PostMapping("/create")
    public ResponseEntity<OwnedPlantDTO> createOwnedLibrary(@RequestBody OwnedPlantDTO OwnedPlantDTO) {
        return null;
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<OwnedPlantDTO> updateOwnedLibrary(@PathVariable String id, @RequestBody OwnedPlantDTO OwnedPlantDTO) {
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<OwnedPlantDTO> deleteOwnedLibrary(@PathVariable String id) {
        return null;
    }
}