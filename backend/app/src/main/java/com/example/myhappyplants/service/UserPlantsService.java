package com.example.myhappyplants.service;

import com.example.myhappyplants.dto.CreateUserPlantRequest;
import com.example.myhappyplants.dto.EditUserPlantRequest;
import com.example.myhappyplants.dto.PatchUserPlantRequest;
import com.example.myhappyplants.dto.UserPlantResponse;
import com.example.myhappyplants.entity.Species;
import com.example.myhappyplants.entity.User;
import com.example.myhappyplants.entity.UserPlant;
import com.example.myhappyplants.repository.SpeciesRepository;
import com.example.myhappyplants.repository.UserPlantRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserPlantsService {
    private static final int ENTRIES_PER_PAGE = 30;

    private final UserService userService;
    private final UserPlantRepository userPlantRepository;
    private final SpeciesRepository speciesRepository;

    public List<UserPlantResponse> allOwnedPlants(Authentication user, int page) {
        return userPlantRepository.findAllByUser(
                        userService.loadUserByUserDetails(user),
                        Pageable.ofSize(ENTRIES_PER_PAGE).withPage(page)
                )
                .stream()
                .map(UserPlantResponse::fromUserPlant)
                .toList();
    }

    public UserPlantResponse getInOwnedLibrary(Authentication user, int id) {
        return userPlantRepository.findUserPlantByUserAndId(userService.loadUserByUserDetails(user), id)
                .map(UserPlantResponse::fromUserPlant)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User plant entry not found"));
    }

    public UserPlantResponse createInOwnedLibrary(Authentication user, CreateUserPlantRequest newUserPlantRequest) {
        Species species = speciesRepository.findById(newUserPlantRequest.trefleId())
                .orElseThrow();

        UserPlant userPlant = new UserPlant();

        userPlant.setUser(userService.loadUserByUserDetails(user));
        userPlant.setLinkedSpecies(species);

        userPlant.setLastWatered(newUserPlantRequest.lastWatered());
        userPlant.setNickname(newUserPlantRequest.nickname());
        userPlant.setWaterFrequency(newUserPlantRequest.waterFrequency());
        userPlant.setPlantDescription(newUserPlantRequest.description());

        return UserPlantResponse.fromUserPlant(userPlantRepository.save(userPlant));
    }

    public boolean deleteInOwnedLibrary(Authentication user, int id) {
        User userEntry = userService.loadUserByUserDetails(user);

        Optional<UserPlant> userPlant = userPlantRepository.findUserPlantByUserAndId(userEntry, id);
        userPlant.ifPresent(userPlantRepository::delete);

        return userPlant.isPresent();
    }

    public UserPlantResponse replaceInOwnedLibrary(Authentication user, int id, EditUserPlantRequest editUserPlantRequest) {
        UserPlant userPlant = userPlantRepository.findUserPlantByUserAndId(userService.loadUserByUserDetails(user), id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User plant entry not found"));

        userPlant.setWaterFrequency(editUserPlantRequest.waterFrequency());
        userPlant.setNickname(editUserPlantRequest.nickname());
        userPlant.setPlantDescription(editUserPlantRequest.description());


        return UserPlantResponse.fromUserPlant(userPlantRepository.save(userPlant));
    }

    public UserPlantResponse updateInOwnedLibrary(Authentication user, int id, PatchUserPlantRequest patchUserPlantRequest) {
        UserPlant userPlant = userPlantRepository.findUserPlantByUserAndId(userService.loadUserByUserDetails(user), id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User plant entry not found"));

        if (patchUserPlantRequest.nickname() != null) {
            userPlant.setNickname(patchUserPlantRequest.nickname());
        }

        if (patchUserPlantRequest.waterFrequency() != null) {
            userPlant.setWaterFrequency(patchUserPlantRequest.waterFrequency());
        }

        if (patchUserPlantRequest.lastWatered() != null) {
            userPlant.setLastWatered(patchUserPlantRequest.lastWatered());
        }

        if (patchUserPlantRequest.description() != null) {
            userPlant.setPlantDescription(patchUserPlantRequest.description());
        }

        return UserPlantResponse.fromUserPlant(userPlantRepository.save(userPlant));
    }
}
