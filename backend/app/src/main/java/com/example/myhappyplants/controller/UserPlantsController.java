package com.example.myhappyplants.controller;

import com.example.myhappyplants.auxillary.StringHelper;
import com.example.myhappyplants.dto.CreateUserPlantRequest;
import com.example.myhappyplants.service.UserPlantsService;
import com.example.myhappyplants.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/owned")
public class UserPlantsController {
    private final UserPlantsService userPlantsService;

    public UserPlantsController(UserPlantsService userPlantsService, UserService userService) {
        this.userPlantsService = userPlantsService;
    }

//TODO - write all the bodies in ResponseEntity().status(HtttpStatus.OK).body(userPlantsService.{METHODNAME()});
    //TODO - the ? (Optional) can also throw other HttpStatuses, like not found etc. Helps inform the response.
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllOwnedLibrary(Authentication user, @RequestParam(defaultValue = "0") int page) {
        return userPlantsService.allOwnedPlants(user, page);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOwnedLibrary(Authentication user, @PathVariable int id) {
        return ResponseEntity.ok(userPlantsService.getInOwnedLibrary(user, id));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createOwnedLibrary(Authentication user, @RequestBody CreateUserPlantRequest editUserPlantRequest) {
        return userPlantsService.createInOwnedLibrary(user, editUserPlantRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateOwnedLibrary(Authentication user, @PathVariable String id, @RequestBody CreateUserPlantRequest editUserPlantRequest) {
        return null;
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteOwnedLibrary(Authentication user, @PathVariable String id) {
        return null;
    }
}