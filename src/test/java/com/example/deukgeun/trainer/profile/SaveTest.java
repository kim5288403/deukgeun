package com.example.deukgeun.trainer.profile;

import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.repository.ProfileRepository;
import com.example.deukgeun.trainer.request.ProfileRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class SaveTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    void shouldCreateProfileFromForValidPathAndUserId() {
        // Given
        String path = "testFileName";
        Long userId = 1L;

        // When
        Profile profile = ProfileRequest.create(path, userId);

        // Then
        assertEquals(profile.getPath(), path);
        assertEquals(profile.getUserId(), userId);
        assertNotNull(profile);
    }

    @Test
    void shouldSaveProfileForValidPathAndUserId() {
        // Given
        String path = "testFileName";
        Long userId = 1L;
        Profile profile = ProfileRequest.create(path, userId);

        // When
        Profile saveProfile = profileRepository.save(profile);

        // Then
        assertEquals(saveProfile.getUserId(), profile.getUserId());
        assertEquals(saveProfile.getPath(), profile.getPath());
        assertNotNull(saveProfile);
    }

    @Test
    void shouldThrowDataIntegrityViolationExceptionForInValidPathAndUserId() {
        // Given
        String path = "";
        Long userId = 0L;
        Profile profile = ProfileRequest.create(path, userId);

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> {
            Profile saveProfile = profileRepository.save(profile);
        });
    }

}
