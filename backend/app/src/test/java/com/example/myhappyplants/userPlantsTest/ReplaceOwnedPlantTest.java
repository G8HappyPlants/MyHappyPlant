package com.example.myhappyplants.userPlantsTest;

import com.example.myhappyplants.dto.EditUserPlantRequest;
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
class ReplaceOwnedPlantTest {

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
        userPlant.setNickname("Old Basil");
        userPlant.setPlantDescription("Old description");
        userPlant.setLastWatered(lastWatered);
        userPlant.setWaterFrequency(10);
        userPlant.setLinkedSpecies(species);

        Set<Tag> existingTags = new HashSet<>();
        existingTags.add(new Tag("OldTag", user));
        userPlant.setTags(existingTags);
    }

    @DisplayName("Test replace with valid input and existing tags - BIB-05-F-1")
    @Test
    void testSuccessfulReplace() {
        EditUserPlantRequest request = new EditUserPlantRequest(
                "New Basil",
                "New description",
                lastWatered,
                5,
                List.of("Kitchen", "Favorite")
        );

        when(userService.loadUserByUserDetails(authentication)).thenReturn(user);
        when(userPlantRepository.findUserPlantByUserAndId(user, 1)).thenReturn(Optional.of(userPlant));
        when(tagRepository.findByNameAndUser("Kitchen", user)).thenReturn(Optional.of(new Tag("Kitchen", user)));
        when(tagRepository.findByNameAndUser("Favorite", user)).thenReturn(Optional.of(new Tag("Favorite", user)));
        when(userPlantRepository.save(any(UserPlant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<UserPlant> userPlantCaptor = ArgumentCaptor.forClass(UserPlant.class);

        UserPlantResponse response = userPlantsService.replaceInOwnedLibrary(authentication, 1, request);

        assertNotNull(response);
        verify(userPlantRepository).save(userPlantCaptor.capture());

        UserPlant savedPlant = userPlantCaptor.getValue();
        assertEquals("New Basil", savedPlant.getNickname());
        assertEquals("New description", savedPlant.getPlantDescription());
        assertEquals(5, savedPlant.getWaterFrequency());
        assertNotNull(savedPlant.getTags());
        assertEquals(2, savedPlant.getTags().size());
    }

    //Kollar i nuläget om tags inte tas bort när man inte matar in tags, borde tags tas bort vi något tillfälle? Hur ser formuläret ut. Går det ta bort tags?
    @DisplayName("Test replace with null tag-list keeps existing tags - BIB-05-F-2")
    @Test
    void testSuccessfulReplaceWithNullTagList() {
        Set<Tag> originalTags = new HashSet<>(userPlant.getTags());

        EditUserPlantRequest request = new EditUserPlantRequest(
                "New Basil",
                "New description",
                lastWatered,
                5,
                null
        );

        when(userService.loadUserByUserDetails(authentication)).thenReturn(user);
        when(userPlantRepository.findUserPlantByUserAndId(user, 1)).thenReturn(Optional.of(userPlant));
        when(userPlantRepository.save(any(UserPlant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<UserPlant> userPlantCaptor = ArgumentCaptor.forClass(UserPlant.class);

        UserPlantResponse response = userPlantsService.replaceInOwnedLibrary(authentication, 1, request);

        assertNotNull(response);
        verify(userPlantRepository).save(userPlantCaptor.capture());
        verifyNoInteractions(tagRepository);

        UserPlant savedPlant = userPlantCaptor.getValue();
        assertEquals("New Basil", savedPlant.getNickname());
        assertEquals("New description", savedPlant.getPlantDescription());
        assertEquals(5, savedPlant.getWaterFrequency());
        assertEquals(originalTags, savedPlant.getTags());
    }

    @DisplayName("Test replace creates missing tags - BIB-05-F-3")
    @Test
    void testSuccessfulReplaceCreatesMissingTags() {
        EditUserPlantRequest request = new EditUserPlantRequest(
                "New Basil",
                "New description",
                lastWatered,
                5,
                List.of("Kitchen", "Favorite")
        );

        when(userService.loadUserByUserDetails(authentication)).thenReturn(user);
        when(userPlantRepository.findUserPlantByUserAndId(user, 1)).thenReturn(Optional.of(userPlant));
        when(tagRepository.findByNameAndUser("Kitchen", user)).thenReturn(Optional.empty());
        when(tagRepository.findByNameAndUser("Favorite", user)).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userPlantRepository.save(any(UserPlant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserPlantResponse response = userPlantsService.replaceInOwnedLibrary(authentication, 1, request);

        assertNotNull(response);
        verify(tagRepository, times(2)).save(any(Tag.class));
        verify(userPlantRepository).save(any(UserPlant.class));
    }

    @DisplayName("Test replace with missing plant throws not found - BIB-05-F-4")
    @Test
    void testReplaceFailsWhenPlantIsMissing() {
        EditUserPlantRequest request = new EditUserPlantRequest(
                "New Basil",
                "New description",
                lastWatered,
                5,
                List.of("Kitchen", "Favorite")
        );

        when(userService.loadUserByUserDetails(authentication)).thenReturn(user);
        when(userPlantRepository.findUserPlantByUserAndId(user, 99)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userPlantsService.replaceInOwnedLibrary(authentication, 99, request)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User plant entry not found", exception.getReason());
        verify(userPlantRepository, never()).save(any(UserPlant.class));
    }
}