package com.example.myhappyplants.userPlantsTest;

import com.example.myhappyplants.dto.PatchUserPlantRequest;
import com.example.myhappyplants.dto.UserPlantResponse;
import com.example.myhappyplants.entity.Species;
import com.example.myhappyplants.entity.Tag;
import com.example.myhappyplants.entity.User;
import com.example.myhappyplants.entity.UserPlant;
import com.example.myhappyplants.repository.SpeciesRepository;
import com.example.myhappyplants.repository.TagRepository;
import com.example.myhappyplants.repository.UserPlantRepository;
import com.example.myhappyplants.service.UserPlantsService;
import com.example.myhappyplants.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateOwnedPlantTest {

    @Mock
    private UserService userService;

    @Mock
    private UserPlantRepository userPlantRepository;

    @Mock
    private SpeciesRepository speciesRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserPlantsService userPlantsService;

    private User user;
    private Species species;
    private UserPlant userPlant;
    private Instant originalLastWatered;
    private Instant updatedLastWatered;

    @BeforeEach
    void setUp() {
        originalLastWatered = Instant.parse("2026-03-01T10:15:30Z");
        updatedLastWatered = Instant.parse("2026-03-10T10:15:30Z");

        user = new User(
                "testuser",
                "valid.user@test.com",
                "emailHash",
                "passwordHash"
        );

        species = new Species(
                10L,
                "Ocimum basilicum",
                "Ocimum",
                "Lamiaceae",
                "Asia",
                true,
                "Basil",
                "Mint family"
        );

        userPlant = new UserPlant();
        userPlant.setUser(user);
        userPlant.setNickname("Old Basil");
        userPlant.setPlantDescription("Old description");
        userPlant.setLastWatered(originalLastWatered);
        userPlant.setWaterFrequency(10);
        userPlant.setLinkedSpecies(species);

        Set<Tag> existingTags = new HashSet<>();
        existingTags.add(new Tag("OldTag", user));
        userPlant.setTags(existingTags);
    }

    @DisplayName("Test partial update only changes provided fields - BIB-06-F-1")
    @Test
    void testPartialUpdateOnlyChangesProvidedFields() {
        PatchUserPlantRequest request = new PatchUserPlantRequest(
                "New Basil",
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(userService.loadUserByUserDetails(authentication)).thenReturn(user);
        when(userPlantRepository.findUserPlantByUserAndId(user, 1)).thenReturn(Optional.of(userPlant));
        when(userPlantRepository.save(any(UserPlant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<UserPlant> userPlantCaptor = ArgumentCaptor.forClass(UserPlant.class);

        UserPlantResponse response = userPlantsService.updateInOwnedLibrary(authentication, 1, request);

        assertNotNull(response);
        verify(userPlantRepository).save(userPlantCaptor.capture());
        verifyNoInteractions(tagRepository);

        UserPlant savedPlant = userPlantCaptor.getValue();
        assertEquals("New Basil", savedPlant.getNickname());
        assertEquals("Old description", savedPlant.getPlantDescription());
        assertEquals(originalLastWatered, savedPlant.getLastWatered());
        assertEquals(10, savedPlant.getWaterFrequency());
        assertEquals(1, savedPlant.getTags().size());
    }

    @DisplayName("Test update changes all provided fields including tags - BIB-06-F-2")
    @Test
    void testUpdateWithAllProvidedFields() {
        PatchUserPlantRequest request = new PatchUserPlantRequest(
                "New Basil",
                "New description",
                updatedLastWatered,
                5,
                List.of("Kitchen", "Favorite"),
                null,
                null
        );

        when(userService.loadUserByUserDetails(authentication)).thenReturn(user);
        when(userPlantRepository.findUserPlantByUserAndId(user, 1)).thenReturn(Optional.of(userPlant));
        when(tagRepository.findByNameAndUser("Kitchen", user)).thenReturn(Optional.of(new Tag("Kitchen", user)));
        when(tagRepository.findByNameAndUser("Favorite", user)).thenReturn(Optional.of(new Tag("Favorite", user)));
        when(userPlantRepository.save(any(UserPlant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<UserPlant> userPlantCaptor = ArgumentCaptor.forClass(UserPlant.class);

        UserPlantResponse response = userPlantsService.updateInOwnedLibrary(authentication, 1, request);

        assertNotNull(response);
        verify(userPlantRepository).save(userPlantCaptor.capture());

        UserPlant savedPlant = userPlantCaptor.getValue();
        assertEquals("New Basil", savedPlant.getNickname());
        assertEquals("New description", savedPlant.getPlantDescription());
        assertEquals(updatedLastWatered, savedPlant.getLastWatered());
        assertEquals(5, savedPlant.getWaterFrequency());
        assertEquals(2, savedPlant.getTags().size());
    }

    @DisplayName("Test update creates missing tags - BIB-06-F-3")
    @Test
    void testUpdateCreatesMissingTags() {
        PatchUserPlantRequest request = new PatchUserPlantRequest(
                null,
                null,
                null,
                null,
                List.of("Kitchen", "Favorite"),
                null,
                null
        );

        when(userService.loadUserByUserDetails(authentication)).thenReturn(user);
        when(userPlantRepository.findUserPlantByUserAndId(user, 1)).thenReturn(Optional.of(userPlant));
        when(tagRepository.findByNameAndUser("Kitchen", user)).thenReturn(Optional.empty());
        when(tagRepository.findByNameAndUser("Favorite", user)).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userPlantRepository.save(any(UserPlant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserPlantResponse response = userPlantsService.updateInOwnedLibrary(authentication, 1, request);

        assertNotNull(response);
        verify(tagRepository, times(2)).save(any(Tag.class));
        verify(userPlantRepository).save(any(UserPlant.class));
    }

    @DisplayName("Test update with missing plant throws not found - BIB-06-F-4")
    @Test
    void testUpdateFailsWhenPlantIsMissing() {
        PatchUserPlantRequest request = new PatchUserPlantRequest(
                "New Basil",
                "New description",
                updatedLastWatered,
                5,
                List.of("Kitchen", "Favorite"),
                null,
                null
        );

        when(userService.loadUserByUserDetails(authentication)).thenReturn(user);
        when(userPlantRepository.findUserPlantByUserAndId(user, 99)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userPlantsService.updateInOwnedLibrary(authentication, 99, request)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User plant entry not found", exception.getReason());
        verify(userPlantRepository, never()).save(any(UserPlant.class));
    }
}