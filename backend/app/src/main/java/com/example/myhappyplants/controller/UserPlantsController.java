package com.example.myhappyplants.controller;

import com.example.myhappyplants.dto.UserPlantDTO;
import com.example.myhappyplants.service.UserPlantsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owned")
public class UserPlantsController {
    private final UserPlantsService userLibraryService;

    public UserPlantsController(UserPlantsService userLibraryService) {this.userLibraryService = userLibraryService;}

    @GetMapping("/all")
    public ResponseEntity<UserPlantDTO> getAllOwnedLibrary() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPlantDTO> getOwnedLibrary(@PathVariable String id) {
        return null;
    }

    @PostMapping("/create")
    public ResponseEntity<UserPlantDTO> createOwnedLibrary(@RequestBody UserPlantDTO UserPlantDTO) {
        return null;
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<UserPlantDTO> updateOwnedLibrary(@PathVariable String id, @RequestBody UserPlantDTO UserPlantDTO) {
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserPlantDTO> deleteOwnedLibrary(@PathVariable String id) {
        return null;
    }
}