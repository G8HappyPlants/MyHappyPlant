package com.example.myhappyplants.userPlantsTest;

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
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteOwnedPlantTest {
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
    private UserPlant userPlant;

    @BeforeEach
    void setUp() {
        user = new User(
                "testuser",
                "valid.user@test.com",
                "emailHash",
                "passwordHash"
        );

        userPlant = new UserPlant();
        userPlant.setUser(user);
        userPlant.setNickname("Kitchen Basil");
    }

    @DisplayName("Test delete with existing owned plant - BIB-04-F-1")
    @Test
    void testSuccessfulDelete() {
        when(userService.loadUserByUserDetails(authentication)).thenReturn(user);
        when(userPlantRepository.findUserPlantByUserAndId(user, 1)).thenReturn(Optional.of(userPlant));

        boolean result = userPlantsService.deleteInOwnedLibrary(authentication, 1);

        assertTrue(result);
        verify(userService).loadUserByUserDetails(authentication);
        verify(userPlantRepository).findUserPlantByUserAndId(user, 1);
        verify(userPlantRepository).delete(userPlant);
    }

    @DisplayName("Test delete with missing owned plant - BIB-04-F-2")
    @Test
    void testDeleteReturnsFalseWhenPlantDoesNotExist() {
        when(userService.loadUserByUserDetails(authentication)).thenReturn(user);
        when(userPlantRepository.findUserPlantByUserAndId(user, 99)).thenReturn(Optional.empty());

        boolean result = userPlantsService.deleteInOwnedLibrary(authentication, 99);

        assertFalse(result);
        verify(userService).loadUserByUserDetails(authentication);
        verify(userPlantRepository).findUserPlantByUserAndId(user, 99);
        verify(userPlantRepository, never()).delete(any(UserPlant.class));
    }
}
