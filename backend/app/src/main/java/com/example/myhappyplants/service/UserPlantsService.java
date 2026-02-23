package com.example.myhappyplants.service;

import com.example.myhappyplants.dto.EditUserPlantRequest;
import com.example.myhappyplants.entity.UserPlant;
import com.example.myhappyplants.repository.UserPlantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPlantsService {
    private final UserService userService;
    private final UserPlantRepository userPlantRepository;

    public UserPlantsService(UserService userService, UserPlantRepository userPlantRepository) {
        this.userService = userService;
        this.userPlantRepository = userPlantRepository;
    }

    private long userToId(Authentication user) { return userService.loadUserByUserDetails(user).getId(); }

    public ResponseEntity<List<UserPlant>> allOwnedPlants(Authentication user) {
        return ResponseEntity.ok(userPlantRepository.findAllByUserId(userToId(user)));
    }

    public ResponseEntity<?> getInOwnedLibrary(Authentication user, int id) {
        return userPlantRepository.findUserPlantByIdAndUserId(id, userToId(user))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> createInOwnedLibrary(Authentication user, EditUserPlantRequest editUserPlantRequest) {
        return null;//return userPlantRepository.save(editUserPlantRequest.toUserPlant())
    }
}
