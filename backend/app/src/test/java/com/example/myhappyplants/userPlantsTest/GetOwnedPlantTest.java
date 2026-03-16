package com.example.myhappyplants.userPlantsTest;

import com.example.myhappyplants.dto.UserPlantResponse;
import com.example.myhappyplants.entity.Species;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetOwnedPlantTest {

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
    private Instant lastWatered;

    @BeforeEach
    void setUp() {
        lastWatered = Instant.parse(Instant.now().toString());

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
        userPlant.setNickname("Kitchen Basil");
        userPlant.setPlantDescription("Healthy and green");
        userPlant.setLastWatered(lastWatered);
        userPlant.setWaterFrequency(7);
        userPlant.setLinkedSpecies(species);
    }

    @DisplayName("Test with existing owned plant - BIB-03-F-1")
    @Test
    void testGetOwnedPlantSuccessfully() {
        when(userService.loadUserByUserDetails(authentication)).thenReturn(user);
        when(userPlantRepository.findUserPlantByUserAndId(user, 1)).thenReturn(Optional.of(userPlant));

        UserPlantResponse response = userPlantsService.getInOwnedLibrary(authentication, 1);

        assertNotNull(response);
        verify(userService).loadUserByUserDetails(authentication);
        verify(userPlantRepository).findUserPlantByUserAndId(user, 1);
    }

    @DisplayName("Test with missing owned plant - BIB-03-F-2")
    @Test
    void testGetOwnedPlantNotFound() {
        when(userService.loadUserByUserDetails(authentication)).thenReturn(user);
        when(userPlantRepository.findUserPlantByUserAndId(user, 99)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userPlantsService.getInOwnedLibrary(authentication, 99)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User plant entry not found", exception.getReason());
        verify(userPlantRepository).findUserPlantByUserAndId(user, 99);
    }
}
