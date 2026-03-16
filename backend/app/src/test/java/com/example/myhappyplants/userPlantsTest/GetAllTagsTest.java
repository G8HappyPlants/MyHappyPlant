package com.example.myhappyplants.userPlantsTest;

import com.example.myhappyplants.dto.TagResponse;
import com.example.myhappyplants.entity.Tag;
import com.example.myhappyplants.entity.User;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllTagsTest {

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

    @DisplayName("Test get all tags with no tags for user - BIB-07-F-1")
    @Test
    void testGetAllTagsReturnsEmptyList() {
        when(userService.loadUserByUserDetails(authentication)).thenReturn(user);
        when(tagRepository.findAllByUser(user)).thenReturn(List.of());

        List<TagResponse> response = userPlantsService.getAllTagsForUser(authentication);

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(userService).loadUserByUserDetails(authentication);
        verify(tagRepository).findAllByUser(user);
    }

    @DisplayName("Test get all tags with existing tags for user - BIB-07-F-2")
    @Test
    void testGetAllTagsReturnsMappedTags() {
        when(userService.loadUserByUserDetails(authentication)).thenReturn(user);

        Tag tag1 = new Tag("Kitchen", user);
        Tag tag2 = new Tag("Favorite", user);

        when(tagRepository.findAllByUser(user)).thenReturn(List.of(tag1, tag2));

        List<TagResponse> response = userPlantsService.getAllTagsForUser(authentication);

        assertNotNull(response);
        assertEquals(2, response.size());
        verify(userService).loadUserByUserDetails(authentication);
        verify(tagRepository).findAllByUser(user);
    }

    @DisplayName("Test get all tags fails when user lookup fails - BIB-07-F-3")
    @Test
    void testGetAllTagsFailsWhenUserLookupFails() {
        when(userService.loadUserByUserDetails(authentication))
                .thenThrow(new RuntimeException("User not found"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userPlantsService.getAllTagsForUser(authentication)
        );

        assertEquals("User not found", exception.getMessage());
        verify(tagRepository, never()).findAllByUser(any(User.class));
    }
}