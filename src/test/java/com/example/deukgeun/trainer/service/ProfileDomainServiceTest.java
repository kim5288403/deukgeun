package com.example.deukgeun.trainer.service;

import com.example.deukgeun.DeukgeunApplication;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.repository.ProfileRepository;
import com.example.deukgeun.trainer.domain.service.implement.ProfileDomainServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DeukgeunApplication.class)
class ProfileDomainServiceTest {
    @InjectMocks
    private ProfileDomainServiceImpl profileDomainService;
    @Mock
    private ProfileRepository profileRepository;

    @Test
    void givenProfileId_whenDeleteById_thenProfileIsDeleted() {
        // Given
        Long profileId = 1L;

        // When
        profileDomainService.deleteById(profileId);

        // Then
        verify(profileRepository, times(1)).deleteById(profileId);
    }

    @Test
    void givenExistingProfileId_whenFindById_thenReturnProfile() {
        // Given
        Long profileId = 1L;
        Profile expectedProfile = new Profile(profileId, 123L, "test");
        given(profileRepository.findById(profileId)).willReturn(Optional.of(expectedProfile));

        // When
        Profile result = profileDomainService.findById(profileId);

        // Then
        assertEquals(expectedProfile, result);
    }

    @Test
    void givenNonExistingProfileId_whenFindById_thenThrowEntityNotFoundException() {
        // Given
        Long profileId = 1L;
        given(profileRepository.findById(profileId)).willReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> profileDomainService.findById(profileId),
                "게시글을 찾을 수 없습니다.");
    }

    @Test
    void givenExistingTrainerId_whenFindByTrainerId_thenReturnProfile() {
        // Given
        Long trainerId = 1L;
        Profile expectedProfile = new Profile(1L, trainerId, "test");

        given(profileRepository.findByTrainerId(trainerId)).willReturn(Optional.of(expectedProfile));

        // When
        Profile result = profileDomainService.findByTrainerId(trainerId);

        // Then
        assertEquals(expectedProfile, result);
    }

    @Test
    void givenNonExistingTrainerId_whenFindByTrainerId_thenThrowEntityNotFoundException() {
        // Given
        Long trainerId = 1L;
        given(profileRepository.findByTrainerId(trainerId)).willReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> profileDomainService.findByTrainerId(trainerId),
                "프로필을 찾을 수 없습니다.");
    }

    @Test
    public void givenFileNameAndTrainerId_whenSave_thenProfileIsSaved() throws IOException {
        // Given
        String fileName = "test.jpg";
        Long trainerId = 123L;
        Profile profile = new Profile(1L, trainerId, fileName);
        given(profileRepository.save(any(Profile.class))).willReturn(profile);

        // When
        Profile result = profileDomainService.save(fileName, trainerId);

        // Then
        assertEquals(profile.getPath(), result.getPath());
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void givenFoundProfileAndPath_whenUpdate_thenProfileIsUpdated() {
        // Given
        String path = "new/path";
        Profile profile = new Profile(1L, 123L, "path");

        // When
        profileDomainService.update(profile, path);

        // Then
        verify(profileRepository, times(1)).save(profile);
        assertEquals(path, profile.getPath());
    }

}
