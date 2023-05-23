package com.example.deukgeun.trainer.profile;

import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.repository.ProfileRepository;
import com.example.deukgeun.trainer.request.ProfileRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class SaveTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    void shouldCreateProfileFromJProfileRequest() {
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
    void shouldSaveProfileForFileNameAndUserId() {
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

}
