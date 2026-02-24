package com.example.myhappyplants.controller;

import com.example.myhappyplants.dto.CreateUserPlantRequest;
import com.example.myhappyplants.dto.EditUserPlantRequest;
import com.example.myhappyplants.dto.PatchUserPlantRequest;
import com.example.myhappyplants.service.UserPlantsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owned")
@RequiredArgsConstructor
public class UserPlantsController {
    private final UserPlantsService userPlantsService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllOwnedPlants(Authentication user, @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(userPlantsService.allOwnedPlants(user, page));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createOwnedPlant(Authentication user, @Valid @RequestBody CreateUserPlantRequest createUserPlantRequest) {
        return ResponseEntity.ok(userPlantsService.createInOwnedLibrary(user, createUserPlantRequest));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOwnedPlant(Authentication user, @PathVariable int id) {
        return ResponseEntity.ok(userPlantsService.getInOwnedLibrary(user, id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> replaceOwnedPlant(Authentication user, @PathVariable int id, @Valid @RequestBody EditUserPlantRequest editUserPlantRequest) {
        return ResponseEntity.ok(userPlantsService.replaceInOwnedLibrary(user, id, editUserPlantRequest));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateOwnedPlant(Authentication user, @PathVariable int id, @Valid @RequestBody PatchUserPlantRequest patchUserPlantRequest) {
        return ResponseEntity.ok(userPlantsService.updateInOwnedLibrary(user, id, patchUserPlantRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteOwnedLibrary(Authentication user, @PathVariable int id) {
        return userPlantsService.deleteInOwnedLibrary(user, id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}