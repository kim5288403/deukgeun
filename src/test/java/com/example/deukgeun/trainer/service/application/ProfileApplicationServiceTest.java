package com.example.deukgeun.trainer.service.application;

import com.example.deukgeun.trainer.application.dto.response.ProfileResponse;
import com.example.deukgeun.trainer.application.service.implement.ProfileApplicationServiceImpl;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.service.TrainerDomainService;
import com.example.deukgeun.trainer.infrastructure.persistence.mapper.ProfileMapper;
import com.example.deukgeun.trainer.infrastructure.s3.S3Service;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProfileApplicationServiceTest {
    @InjectMocks
    private ProfileApplicationServiceImpl profileApplicationService;
    @Mock
    private TrainerDomainService trainerDomainService;
    @Mock
    private S3Service s3Service;
    @Mock
    private ProfileMapper profileMapper;

    @Test
    public void givenValidEmail_whenGetProfileByEmail_thenReturnProfileResponse() {
        // Given
        String email = "example@example.com";
        String path = "/path/to/profile";
        Trainer trainer = mock(Trainer.class);
        Profile profile = Profile.create(path);
        ProfileResponse profileResponse = new ProfileResponse(path);

        given(trainerDomainService.findByEmail(anyString())).willReturn(trainer);
        given(profileMapper.toProfileResponse(any(Profile.class))).willReturn(profileResponse);
        given(trainer.getProfile()).willReturn(profile);

        // When
        ProfileResponse response = profileApplicationService.getProfileByEmail(email);

        // Then
        assertNotNull(response);
        assertEquals(path, response.getPath());
        verify(trainerDomainService, times(1)).findByEmail(anyString());
    }

    @Test
    public void givenValidEmailAndProfile_whenUpdateProfile_thenS3ServiceUploadAndDeleteCalled() throws Exception {
        // Given
        String email = "test@example.com";
        String originalFilename = "profile.jpg";
        String expectedFileName = "/path/to/profile/files/your_generated_uuid.jpg";
        String existingPath = "/path/to/profile/files/your_generated_uuid.jpg";

        MultipartFile profileMock = mock(MultipartFile.class);

        given(profileMock.getOriginalFilename()).willReturn(originalFilename);
        given(profileMock.getContentType()).willReturn("image/jpeg");
        given(s3Service.uploadByMultiPartFile(any(MultipartFile.class))).willReturn(expectedFileName);
        given(trainerDomainService.updateProfileByEmail(anyString(), anyString())).willReturn(existingPath);

        // When
        profileApplicationService.updateProfile(email, profileMock);

        // Then
        verify(s3Service, times(1)).delete(anyString());
        verify(s3Service, times(1)).uploadByMultiPartFile(any(MultipartFile.class));
        verify(trainerDomainService, times(1)).updateProfileByEmail(anyString(), anyString());
    }
}
