package com.example.myhappyplants.service;

import com.example.myhappyplants.dto.CreateUserPlantRequest;
import com.example.myhappyplants.entity.UserPlant;
import com.example.myhappyplants.repository.UserPlantRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPlantsService {
    private static int ENTRIES_PER_PAGE = 30;

    private final UserService userService;
    private final UserPlantRepository userPlantRepository;

    public UserPlantsService(UserService userService, UserPlantRepository userPlantRepository) {
        this.userService = userService;
        this.userPlantRepository = userPlantRepository;
    }

    private long userToId(Authentication user) { return userService.loadUserByUserDetails(user).getId(); }

    public ResponseEntity<List<UserPlant>> allOwnedPlants(Authentication user, int page) {
        return ResponseEntity.ok(
                userPlantRepository.findAllByUserId(
                        userToId(user),
                        Pageable.ofSize(ENTRIES_PER_PAGE).withPage(page)
                )
        );
    }

    public ResponseEntity<?> getInOwnedLibrary(Authentication user, int id) {
        return userPlantRepository.findUserPlantByIdAndUserId(id, userToId(user))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> createInOwnedLibrary(Authentication user, CreateUserPlantRequest editUserPlantRequest) {
        return null;//return userPlantRepository.save(editUserPlantRequest.toUserPlant())
    }
}
