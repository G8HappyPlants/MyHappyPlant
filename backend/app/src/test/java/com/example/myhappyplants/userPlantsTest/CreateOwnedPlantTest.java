package com.example.myhappyplants.userPlantsTest;

import com.example.myhappyplants.dto.CreateUserPlantRequest;
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
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOwnedPlantTest {

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
    private CreateUserPlantRequest createRequest;
    private Instant lastWatered;

    @BeforeEach
    void setUp(){
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

        createRequest = new CreateUserPlantRequest(
                "Kitchen Basil",
                "Healthy and green",
                lastWatered,
                7,
                10L,
                List.of("Basil", "Kitchen", "Sensitive"),
                null,
                null
        );
    }

    private void stubValidCreateDependencies() {
        when(userService.loadUserByUserDetails(authentication)).thenReturn(user);
        when(speciesRepository.findById(createRequest.trefleId())).thenReturn(Optional.of(species));
        when(userPlantRepository.save(any(UserPlant.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @DisplayName("Test with valid input - BIB-01-F-1")
    @Test
    void testSuccessfulCreate() {
        stubValidCreateDependencies();


        when(tagRepository.findByNameAndUser("Basil", user))
                .thenReturn(Optional.of(new Tag("Basil", user)));
        when(tagRepository.findByNameAndUser("Kitchen", user))
                .thenReturn(Optional.of(new Tag("Kitchen", user)));
        when(tagRepository.findByNameAndUser("Sensitive", user))
                .thenReturn(Optional.of(new Tag("Sensitive", user)));

        ArgumentCaptor<UserPlant> userPlantCaptor = ArgumentCaptor.forClass(UserPlant.class);

        UserPlantResponse response = userPlantsService.createInOwnedLibrary(authentication, createRequest);

        assertNotNull(response);
        verify(userPlantRepository).save(userPlantCaptor.capture());


        UserPlant savedPlant = userPlantCaptor.getValue();
        assertEquals(user, savedPlant.getUser());
        assertEquals(species, savedPlant.getLinkedSpecies());
        assertEquals("Kitchen Basil", savedPlant.getNickname());
        assertEquals("Healthy and green", savedPlant.getPlantDescription());
        assertEquals(lastWatered, savedPlant.getLastWatered());
        assertEquals(7, savedPlant.getWaterFrequency());
        assertNotNull(savedPlant.getTags());
        assertEquals(3, savedPlant.getTags().size());
    }

    @DisplayName("Test with null tag-list - BIB-01-F-2")
    @Test
    void testSuccessfulCreateWithNullTagList() {
        CreateUserPlantRequest requestWithoutTags = new CreateUserPlantRequest(
                "Kitchen Basil",
                "Healthy and green",
                lastWatered,
                7,
                10L,
                null,
                null,
                null
        );

        stubValidCreateDependencies();

        ArgumentCaptor<UserPlant> userPlantCaptor = ArgumentCaptor.forClass(UserPlant.class);

        UserPlantResponse response = userPlantsService.createInOwnedLibrary(authentication, requestWithoutTags);

        assertNotNull(response);
        verify(userPlantRepository).save(userPlantCaptor.capture());
        verifyNoInteractions(tagRepository);
        verify(userPlantRepository).save(any(UserPlant.class));
    }


    @DisplayName("Testing trimming of tag-names - BIB-01-F-3")
    @Test
    void testSuccessfulCreateWithTrimmedTagNames() {
        CreateUserPlantRequest requestWithTrimmedTags = new CreateUserPlantRequest(
                "Kitchen Basil",
                "Healthy and green",
                lastWatered,
                7,
                10L,
                List.of("     Basil ", " Kitchen ", " Sensitive "),
                null,
                null
        );

        stubValidCreateDependencies();

        when(tagRepository.findByNameAndUser("Basil", user))
                .thenReturn(Optional.of(new Tag("Basil", user)));
        when(tagRepository.findByNameAndUser("Kitchen", user))
                .thenReturn(Optional.of(new Tag("Kitchen", user)));
        when(tagRepository.findByNameAndUser("Sensitive", user))
                .thenReturn(Optional.of(new Tag("Sensitive", user)));

        userPlantsService.createInOwnedLibrary(authentication, requestWithTrimmedTags);

        verify(tagRepository).findByNameAndUser("Basil", user);
        verify(tagRepository).findByNameAndUser("Kitchen", user);
        verify(tagRepository).findByNameAndUser("Sensitive", user);
    }

    @DisplayName("Testing missing species - BIB-01-F-4")
    @Test
    void testFailedCreateWithMissingSpecies() {
        when(speciesRepository.findById(createRequest.trefleId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userPlantsService.createInOwnedLibrary(authentication, createRequest));

        verify(userPlantRepository, never()).save(any(UserPlant.class));
        verifyNoInteractions(tagRepository);
    }

    @Test
    @DisplayName("Testing reuse of existing tags - BIB-01-F-5")
    void testSuccessfulCreateWithExistingTagsDoesNotCreateNewOnes() {
        stubValidCreateDependencies();

        when(tagRepository.findByNameAndUser("Basil", user))
                .thenReturn(Optional.of(new Tag("Basil", user)));
        when(tagRepository.findByNameAndUser("Kitchen", user))
                .thenReturn(Optional.of(new Tag("Kitchen", user)));
        when(tagRepository.findByNameAndUser("Sensitive", user))
                .thenReturn(Optional.of(new Tag("Sensitive", user)));

        userPlantsService.createInOwnedLibrary(authentication, createRequest);

        verify(tagRepository, never()).save(any(Tag.class));
        verify(userPlantRepository).save(any(UserPlant.class));
    }

    @Test
    @DisplayName("Testing creation of missing tags - BIB-01-F-6")
    void testSuccessfulCreateCreatesMissingTags() {
        stubValidCreateDependencies();

        when(tagRepository.findByNameAndUser("Basil", user)).thenReturn(Optional.empty());
        when(tagRepository.findByNameAndUser("Kitchen", user)).thenReturn(Optional.empty());
        when(tagRepository.findByNameAndUser("Sensitive", user)).thenReturn(Optional.empty());

        when(tagRepository.save(any(Tag.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        userPlantsService.createInOwnedLibrary(authentication, createRequest);

        verify(tagRepository, times(3)).save(any(Tag.class));
        verify(userPlantRepository).save(any(UserPlant.class));
    }
}