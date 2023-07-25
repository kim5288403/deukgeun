package com.example.deukgeun.trainer.service.application;

import com.example.deukgeun.DeukgeunApplication;
import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.trainer.application.service.implement.ProfileApplicationServiceImpl;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.model.entity.Trainer;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
import com.example.deukgeun.trainer.domain.service.ProfileDomainService;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DeukgeunApplication.class)
class ProfileApplicationServiceTest {
    @InjectMocks
    private ProfileApplicationServiceImpl profileApplicationService;
    @Mock
    private ProfileDomainService profileDomainService;

    @Test
    void givenProfileId_whenDeleteById_thenProfileIsDeleted() {
        // Given
        Long profileId = 1L;

        // When
        profileApplicationService.deleteById(profileId);

        // Then
        verify(profileDomainService, times(1)).deleteById(profileId);
    }

    @Test
    void givenExistingFile_whenDeleteFileToDirectory_thenFileIsDeleted() throws IOException {
        // Given
        String fileName = "test.txt";
        Path tempDir = Files.createTempDirectory("test");
        Path filePath = tempDir.resolve(fileName);
        Files.createFile(filePath);
        ReflectionTestUtils.setField(profileApplicationService, "FILE_PATH", tempDir.toString());

        // When
        profileApplicationService.deleteFileToDirectory(fileName);

        // Then
        assertFalse(Files.exists(filePath));
    }

    @Test
    void givenExistingProfileId_whenFindById_thenReturnProfile() {
        // Given
        Long profileId = 1L;
        Profile expectedProfile = new Profile(profileId, 123L, "test");
        given(profileDomainService.findById(profileId)).willReturn(expectedProfile);

        // When
        Profile result = profileApplicationService.findById(profileId);

        // Then
        assertEquals(expectedProfile, result);
    }

    @Test
    void givenNonExistingProfileId_whenFindById_thenThrowEntityNotFoundException() {
        // Given
        Long profileId = 1L;
        given(profileDomainService.findById(profileId)).willThrow(new EntityNotFoundException());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> profileApplicationService.findById(profileId),
                "게시글을 찾을 수 없습니다.");
    }

    @Test
    void givenExistingTrainerId_whenFindByTrainerId_thenReturnProfile() {
        // Given
        Long trainerId = 1L;
        Profile expectedProfile = new Profile(1L, trainerId, "test");

        given(profileDomainService.findByTrainerId(trainerId)).willReturn(expectedProfile);

        // When
        Profile result = profileApplicationService.findByTrainerId(trainerId);

        // Then
        assertEquals(expectedProfile, result);
    }

    @Test
    void givenNonExistingTrainerId_whenFindByTrainerId_thenThrowEntityNotFoundException() {
        // Given
        Long trainerId = 1L;
        given(profileDomainService.findByTrainerId(trainerId)).willThrow(new EntityNotFoundException());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> profileApplicationService.findByTrainerId(trainerId),
                "프로필을 찾을 수 없습니다.");
    }

    @Test
    void givenValidFileName_whenGetUUIDPath_thenUUIDPathIsReturned() throws IOException {
        // Given
        String fileName = "image.png";

        // When
        String uuidPath = profileApplicationService.getUUIDPath(fileName);

        // Then
        String expectedPath = UUID.randomUUID() + "_" + fileName;
        assertNotEquals(expectedPath, uuidPath);
    }

    @Test
    void givenEmptyFileName_whenGetUUIDPath_thenIOExceptionIsThrown() {
        // Given
        String fileName = "";

        // When/Then
        assertThrows(IOException.class, () -> profileApplicationService.getUUIDPath(fileName));
    }

    @Test
    void givenSupportedFileType_whenIsSupportedContentType_thenReturnTrue() {
        // Given
        MultipartFile file = new MockMultipartFile("file", "image.png", "image/png", new byte[0]);

        // When
        boolean result = profileApplicationService.isSupportedContentType(file);

        // Then
        assertTrue(result);
    }

    @Test
    void givenUnsupportedFileType_whenIsSupportedContentType_thenReturnFalse() {
        // Given
        MultipartFile file = new MockMultipartFile("file", "document.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", new byte[0]);

        // When
        boolean result = profileApplicationService.isSupportedContentType(file);

        // Then
        assertFalse(result);
    }

    @Test
    void givenProfileAndTrainerId_whenSave_thenProfileIsSavedAndFileIsSaved() throws IOException {
        // Given
        Long trainerId = 1L;
        String fileName = "image.png";
        MultipartFile file = new MockMultipartFile("file", fileName, "image/png", new byte[0]);
        Path tempDir = Files.createTempDirectory("test");
        ReflectionTestUtils.setField(profileApplicationService, "FILE_PATH", tempDir.toString());

        // When
        profileApplicationService.save(file, trainerId);

        // Then
        verify(profileDomainService, times(1)).save(any(String.class), anyLong());
    }

    @Test
    void givenFileAndFileName_whenSaveFileToDirectory_thenFileIsSaved() throws IOException {
        // Given
        String fileName = "image.png";
        MultipartFile file = new MockMultipartFile("file", fileName, "image/png", new byte[0]);
        Path tempDir = Files.createTempDirectory("test");
        ReflectionTestUtils.setField(profileApplicationService, "FILE_PATH", tempDir.toString());

        // When
        profileApplicationService.saveFileToDirectory(file, fileName);

        // Then
        Path filePath = tempDir.resolve(fileName);
        assertTrue(Files.exists(filePath));
    }

    @Test
    void givenProfileAndAuthToken_whenUpdateProfile_thenFileIsSavedAndProfileIsUpdated() throws Exception {
        // Given
        Long profileId = 1L;
        String fileName = "image.png";
        MultipartFile file = new MockMultipartFile("file", fileName, "image/png", new byte[0]);
        Path tempDir = Files.createTempDirectory("test");
        ReflectionTestUtils.setField(profileApplicationService, "FILE_PATH", tempDir.toString());

        Trainer trainer = new Trainer (
                123L,
                "test",
                "test",
                "test",
                GroupStatus.N,
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                Gender.M,
                3000,
                "test"
        );

        Profile profile = new Profile(profileId, trainer.getId(), fileName);

        given(profileDomainService.findById(profileId)).willReturn(profile);

        // When
        profileApplicationService.updateProfile(file, profileId);

        // Then
        verify(profileDomainService, times(1)).findById(anyLong());
        verify(profileDomainService, times(1)).update(any(Profile.class), any(String.class));
    }

    @Test
    void givenFoundProfileAndPath_whenUpdate_thenProfileIsUpdated() {
        // Given
        String path = "new/path";
        Profile profile = new Profile(1L, 123L, path);


        // When
        profileApplicationService.update(profile, path);

        // Then
        verify(profileDomainService, times(1)).update(profile, path);
        assertEquals(path, profile.getPath());
    }
}
