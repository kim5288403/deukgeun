package com.example.deukgeun.trainer.service;

import com.example.deukgeun.DeukgeunApplication;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.entity.Trainer;
import com.example.deukgeun.trainer.repository.ProfileRepository;
import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import com.example.deukgeun.trainer.service.implement.TrainerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DeukgeunApplication.class)
class ProfileServiceTest {
    @InjectMocks
    private ProfileServiceImpl profileService;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private TrainerServiceImpl trainerService;

    @Test
    void givenExistingProfileId_whenGetProfile_thenReturnProfile() {
        // Given
        Long profileId = 1L;
        Profile expectedProfile = Profile
                .builder()
                .id(profileId)
                .path("John Doe")
                .build();
        given(profileRepository.findById(profileId)).willReturn(Optional.of(expectedProfile));

        // When
        Profile result = profileService.getProfile(profileId);

        // Then
        assertEquals(expectedProfile, result);
    }

    @Test
    void givenNonExistingProfileId_whenGetProfile_thenThrowEntityNotFoundException() {
        // Given
        Long profileId = 1L;
        given(profileRepository.findById(profileId)).willReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> profileService.getProfile(profileId),
                "게시글을 찾을 수 없습니다.");
    }

    @Test
    void givenValidAuthToken_whenGetProfileId_thenReturnProfileId() {
        // Given
        String authToken = "validAuthToken";
        Trainer trainer = Trainer
                .builder()
                .id(1L)
                .name("John Doe")
                .build();

        Profile profile = Profile
                .builder()
                .id(1L)
                .trainerId(1L)
                .path("John Doe")
                .build();

        given(trainerService.getByAuthToken(authToken)).willReturn(trainer);
        given(profileRepository.findByTrainerId(trainer.getId())).willReturn(Optional.ofNullable(profile));

        // When
        Long result = profileService.getProfileId(authToken);

        // Then
        assertEquals(profile.getId(), result);
    }

    @Test
    void givenExistingTrainerId_whenGetByTrainerId_thenReturnProfile() {
        // Given
        Long trainerId = 1L;
        Profile expectedProfile = Profile
                .builder()
                .id(1L)
                .trainerId(1L)
                .build();

        given(profileRepository.findByTrainerId(trainerId)).willReturn(Optional.of(expectedProfile));

        // When
        Profile result = profileService.getByTrainerId(trainerId);

        // Then
        assertEquals(expectedProfile, result);
    }

    @Test
    void givenNonExistingTrainerId_whenGetByTrainerId_thenThrowEntityNotFoundException() {
        // Given
        Long trainerId = 1L;
        given(profileRepository.findByTrainerId(trainerId)).willReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> profileService.getByTrainerId(trainerId),
                "프로필을 찾을 수 없습니다.");
    }

    @Test
    void givenSupportedFileType_whenIsSupportedContentType_thenReturnTrue() {
        // Given
        MultipartFile file = new MockMultipartFile("file", "image.png", "image/png", new byte[0]);

        // When
        boolean result = profileService.isSupportedContentType(file);

        // Then
        assertTrue(result);
    }

    @Test
    void givenUnsupportedFileType_whenIsSupportedContentType_thenReturnFalse() {
        // Given
        MultipartFile file = new MockMultipartFile("file", "document.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", new byte[0]);

        // When
        boolean result = profileService.isSupportedContentType(file);

        // Then
        assertFalse(result);
    }

    @Test
    void givenFileAndFileName_whenSaveFileToDirectory_thenFileIsSaved() throws IOException {
        // Given
        String fileName = "image.png";
        MultipartFile file = new MockMultipartFile("file", fileName, "image/png", new byte[0]);
        Path tempDir = Files.createTempDirectory("test");
        ReflectionTestUtils.setField(profileService, "FILE_PATH", tempDir.toString());

        // When
        profileService.saveFileToDirectory(file, fileName);

        // Then
        Path filePath = tempDir.resolve(fileName);
        assertTrue(Files.exists(filePath));
    }

    @Test
    void givenExistingFile_whenDeleteFileToDirectory_thenFileIsDeleted() throws IOException {
        // Given
        String fileName = "test.txt";
        Path tempDir = Files.createTempDirectory("test");
        Path filePath = tempDir.resolve(fileName);
        Files.createFile(filePath);
        ReflectionTestUtils.setField(profileService, "FILE_PATH", tempDir.toString());

        // When
        profileService.deleteFileToDirectory(fileName);

        // Then
        assertFalse(Files.exists(filePath));
    }

    @Test
    void givenProfileAndTrainerId_whenSave_thenProfileIsSavedAndFileIsSaved() throws IOException {
        // Given
        Long trainerId = 1L;
        String fileName = "image.png";
        MultipartFile file = new MockMultipartFile("file", fileName, "image/png", new byte[0]);
        Path tempDir = Files.createTempDirectory("test");
        ReflectionTestUtils.setField(profileService, "FILE_PATH", tempDir.toString());

        // When
        profileService.save(file, trainerId);

        // Then
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void givenProfileAndAuthToken_whenUpdateProfile_thenFileIsSavedAndProfileIsUpdated() throws Exception {
        // Given
        String authToken = "testToken";
        Long profileId = 1L;
        String fileName = "image.png";
        MultipartFile file = new MockMultipartFile("file", fileName, "image/png", new byte[0]);
        Path tempDir = Files.createTempDirectory("test");
        ReflectionTestUtils.setField(profileService, "FILE_PATH", tempDir.toString());

        Trainer trainer = Trainer
                .builder()
                .id(123L)
                .email("johndoe@example.com")
                .build();

        Profile profile = Profile
                .builder()
                .trainerId(trainer.getId())
                .id(profileId)
                .path(fileName)
                .build();

        given(trainerService.getByAuthToken(authToken)).willReturn(trainer);
        given(profileRepository.findByTrainerId(trainer.getId())).willReturn(Optional.ofNullable(profile));
        given(profileRepository.findById(profileId)).willReturn(Optional.ofNullable(profile));

        // When
        profileService.updateProfile(file, authToken);

        // Then
        verify(profileRepository, times(1)).findById(anyLong());
        verify(profileRepository, times(1)).findByTrainerId(anyLong());
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void givenFoundProfileAndPath_whenUpdate_thenProfileIsUpdated() {
        // Given
        String path = "new/path";
        Profile foundProfile = Profile
                .builder()
                .id(1L)
                .path("old/path")
                .build();

        // When
        profileService.update(foundProfile, path);

        // Then
        verify(profileRepository, times(1)).save(foundProfile);
        assertEquals(path, foundProfile.getPath());
    }

    @Test
    void givenProfileId_whenWithdrawal_thenProfileIsDeleted() {
        // Given
        Long profileId = 1L;

        // When
        profileService.withdrawal(profileId);

        // Then
        verify(profileRepository, times(1)).deleteById(profileId);
    }

    @Test
    void givenValidFileName_whenGetUUIDPath_thenUUIDPathIsReturned() throws IOException {
        // Given
        String fileName = "image.png";

        // When
        String uuidPath = profileService.getUUIDPath(fileName);

        // Then
        String expectedPath = UUID.randomUUID() + "_" + fileName;
        assertNotEquals(expectedPath, uuidPath);
    }

    @Test
    void givenEmptyFileName_whenGetUUIDPath_thenIOExceptionIsThrown() {
        // Given
        String fileName = "";

        // When/Then
        assertThrows(IOException.class, () -> profileService.getUUIDPath(fileName));
    }
}
