package com.example.myhappyplants.service;

import com.example.myhappyplants.dto.*;
import com.example.myhappyplants.entity.Species;
import com.example.myhappyplants.entity.Tag;
import com.example.myhappyplants.entity.User;
import com.example.myhappyplants.entity.UserPlant;
import com.example.myhappyplants.repository.SpeciesRepository;
import com.example.myhappyplants.repository.TagRepository;
import com.example.myhappyplants.repository.UserPlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class UserPlantsService {
    private static final int ENTRIES_PER_PAGE = 30;

    private final UserService userService;
    private final UserPlantRepository userPlantRepository;
    private final SpeciesRepository speciesRepository;
    private final TagRepository tagRepository;

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
        User userEntry = userService.loadUserByUserDetails(user);
        UserPlant userPlant = new UserPlant();

        userPlant.setUser(userEntry);
        userPlant.setLinkedSpecies(species);

        userPlant.setLastWatered(newUserPlantRequest.lastWatered());
        userPlant.setNickname(newUserPlantRequest.nickname());
        userPlant.setWaterFrequency(newUserPlantRequest.waterFrequency());

        if (newUserPlantRequest.lastWatered() != null && newUserPlantRequest.waterFrequency() != null) {
            userPlant.setNextWaterDate(
                    newUserPlantRequest.lastWatered()
                            .atZone(ZoneId.systemDefault()).toLocalDate()
                            .plusDays(newUserPlantRequest.waterFrequency())
            );
        }

        userPlant.setPlantDescription(newUserPlantRequest.description());

        if (newUserPlantRequest.tagNames() != null) {
            userPlant.setTags(resolveOrCreateTags(userEntry, newUserPlantRequest.tagNames()));
        }

        if (newUserPlantRequest.imageBase64() != null && newUserPlantRequest.imageContentType() != null) {
            userPlant.setImageData(Base64.getDecoder().decode(newUserPlantRequest.imageBase64()));
            userPlant.setImageContentType(newUserPlantRequest.imageContentType());
        }

        UserPlant saved = userPlantRepository.save(userPlant);
        if (saved.getImageData() != null) {
            saved.setImageUrl("/api/owned/" + saved.getId() + "/image");
            userPlantRepository.save(saved);
        }
        return UserPlantResponse.fromUserPlant(saved);
    }

    public boolean deleteInOwnedLibrary(Authentication user, int id) {
        User userEntry = userService.loadUserByUserDetails(user);

        Optional<UserPlant> userPlant = userPlantRepository.findUserPlantByUserAndId(userEntry, id);
        userPlant.ifPresent(userPlantRepository::delete);

        return userPlant.isPresent();
    }

    public UserPlantResponse replaceInOwnedLibrary(Authentication user, int id, EditUserPlantRequest editUserPlantRequest) {
        User userEntry = userService.loadUserByUserDetails(user);
        UserPlant userPlant = userPlantRepository.findUserPlantByUserAndId(userEntry, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User plant entry not found"));

        userPlant.setWaterFrequency(editUserPlantRequest.waterFrequency());
        userPlant.setNickname(editUserPlantRequest.nickname());

        if (userPlant.getLastWatered() != null && userPlant.getWaterFrequency() != null) {
            userPlant.setNextWaterDate(
                    userPlant.getLastWatered()
                            .atZone(ZoneId.systemDefault()).toLocalDate()
                            .plusDays(userPlant.getWaterFrequency())
            );
        }

        userPlant.setPlantDescription(editUserPlantRequest.description());


        if (editUserPlantRequest.tagNames() != null) {
            userPlant.setTags(resolveOrCreateTags(userEntry, editUserPlantRequest.tagNames()));
        }
        return UserPlantResponse.fromUserPlant(userPlantRepository.save(userPlant));
    }

    public UserPlantResponse updateInOwnedLibrary(Authentication user, int id, PatchUserPlantRequest patchUserPlantRequest) {
        User userEntry = userService.loadUserByUserDetails(user);
        UserPlant userPlant = userPlantRepository.findUserPlantByUserAndId(userEntry, id)
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

        if ((patchUserPlantRequest.waterFrequency() != null || patchUserPlantRequest.lastWatered() != null) &&
             userPlant.getWaterFrequency() != null && userPlant.getLastWatered() != null) {

            userPlant.setNextWaterDate(
                    userPlant.getLastWatered()
                            .atZone(ZoneId.systemDefault()).toLocalDate()
                            .plusDays(userPlant.getWaterFrequency())
            );
        }

        if (patchUserPlantRequest.description() != null) {
            userPlant.setPlantDescription(patchUserPlantRequest.description());
        }

        if (patchUserPlantRequest.tagNames() != null) {
            userPlant.setTags(resolveOrCreateTags(userEntry, patchUserPlantRequest.tagNames()));
        }

        if (patchUserPlantRequest.imageBase64() != null && patchUserPlantRequest.imageContentType() != null) {
            userPlant.setImageData(Base64.getDecoder().decode(patchUserPlantRequest.imageBase64()));
            userPlant.setImageContentType(patchUserPlantRequest.imageContentType());
            userPlant.setImageUrl("/api/owned/" + userPlant.getId() + "/image");
        }

        return UserPlantResponse.fromUserPlant(userPlantRepository.save(userPlant));
    }

    public List<TagResponse> getAllTagsForUser(Authentication user) {
        return tagRepository.findAllByUser(userService.loadUserByUserDetails(user))
                .stream()
                .map(TagResponse::fromTag)
                .toList();
    }

    private Set<Tag> resolveOrCreateTags(User user, List<String> tagNames) {
        Set<Tag> tags = new HashSet<>();
        for (String name : tagNames) {
            String trimmed = name.trim();
            Tag tag = tagRepository.findByNameAndUser(trimmed, user)
                    .orElseGet(() -> tagRepository.save(new Tag(trimmed, user)));
            tags.add(tag);
        }
        return tags;
    }

    public ResponseEntity<byte[]> getImage(Authentication auth, int id) {
        User entryUser = userService.loadUserByUserDetails(auth);
        UserPlant plant = userPlantRepository.findUserPlantByUserAndId(entryUser, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plant not found"));

        if (plant.getImageData() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No image uploaded");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(plant.getImageContentType()))
                .body(plant.getImageData());
    }


}
