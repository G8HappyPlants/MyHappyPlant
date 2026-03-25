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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAllOwnedPlantsTest {

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

    @BeforeEach
    void setUp() {
        user = new User(
                "testuser",
                "valid.user@test.com",
                "emailHash",
                "passwordHash"
        );
    }

    @DisplayName("Test with no owned plants - BB-02-F-1")
    @Test
    void testGetAllOwnedPlantsReturningEmptyList() {
        when(userService.loadUserByUserDetails(authentication)).thenReturn(user);
        when(userPlantRepository.findAllByUser(eq(user), any(Pageable.class)))
                .thenReturn(List.of());

        List<UserPlantResponse> response = userPlantsService.allOwnedPlants(authentication, 0);

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(userPlantRepository).findAllByUser(eq(user), any(Pageable.class));
    }

    @DisplayName("Test with existing owned plants - BIB-02-F-2")
    @Test
    void testGetAllOwnedPlantsMapped() {
        when(userService.loadUserByUserDetails(authentication)).thenReturn(user);

        UserPlant plant1 = new UserPlant();
        plant1.setNickname("Kitchen Basil");
        plant1.setLinkedSpecies(new Species(
                10L,
                "Ocimum basilicum",
                "Ocimum",
                "Lamiaceae",
                "Asia",
                true,
                "Basil",
                "Mint family"
        ));

        UserPlant plant2 = new UserPlant();
        plant2.setNickname("Palettblad Livingroom.");
        plant2.setLinkedSpecies(new Species(
                11L,
                "Palettblad?",
                "Något",
                "Någonting?",
                "Någonstans",
                false,
                "Palettblad",
                "Krukväxt"
        ));

        when(userPlantRepository.findAllByUser(eq(user), any(Pageable.class)))
                .thenReturn(List.of(plant1, plant2));

        List<UserPlantResponse> response = userPlantsService.allOwnedPlants(authentication, 0);

        assertNotNull(response);
        assertEquals(2, response.size());
        verify(userPlantRepository).findAllByUser(eq(user), any(Pageable.class));
    }

    @DisplayName("Test that correct pageable is used - BIB-02-F-3")
    @Test
    void testGetAllOwnedPlantsWithPageable() {
        when(userService.loadUserByUserDetails(authentication)).thenReturn(user);

        userPlantsService.allOwnedPlants(authentication, 2);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(userPlantRepository).findAllByUser(eq(user), pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(2, capturedPageable.getPageNumber());
        assertEquals(30, capturedPageable.getPageSize());
    }

    @DisplayName("Test when user not found - BIB-02-F-4")
    @Test
    void testGetAllOwnedPlantsUserNotFound() {
        when(userService.loadUserByUserDetails(authentication)).thenThrow(new RuntimeException("User not found"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userPlantsService.allOwnedPlants(authentication, 0));

        assertEquals("User not found", ex.getMessage());
        verify(userPlantRepository, never()).findAllByUser(any(), any(Pageable.class));
    }
}
