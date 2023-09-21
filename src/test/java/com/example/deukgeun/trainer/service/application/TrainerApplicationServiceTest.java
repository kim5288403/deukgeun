package com.example.deukgeun.trainer.service.application;

import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.service.implement.TrainerApplicationServiceImpl;
import com.example.deukgeun.trainer.domain.dto.SaveTrainerDTO;
import com.example.deukgeun.trainer.domain.dto.UpdateInfoDTO;
import com.example.deukgeun.trainer.domain.dto.UpdatePasswordDTO;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.service.TrainerDomainService;
import com.example.deukgeun.trainer.infrastructure.persistence.mapper.TrainerMapper;
import com.example.deukgeun.trainer.infrastructure.s3.S3Service;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
class TrainerApplicationServiceTest {
    @InjectMocks
    private TrainerApplicationServiceImpl trainerApplicationService;
    @Mock
    private TrainerDomainService trainerDomainService;
    @Mock
    private S3Service s3Service;
    @Mock
    private TrainerMapper trainerMapper;

    @Test
    void givenValidEmail_whenDelete_thenS3ServiceAndTrainerDeleteCalled() {
        // Given
        String email = "email";
        Trainer trainer = mock(Trainer.class);
        Profile profile = Profile.create("path");

        given(trainer.getEmail()).willReturn(email);
        given(trainer.getProfile()).willReturn(profile);
        given(trainerDomainService.findByEmail(anyString())).willReturn(trainer);

        // When
        trainerApplicationService.delete(email);

        // Verify
        verify(s3Service, times(1)).delete(anyString());
        verify(trainerDomainService, times(1)).deleteById(anyLong());
    }

    @Test
    void givenValidEmail_whenExistsByEmail_thenReturnTrue() {
        // Given
        String email = "johndoe@example.com";

        given(trainerDomainService.existsByEmail(anyString())).willReturn(true);

        // When
        boolean result = trainerApplicationService.existsByEmail(email);

        // Then
        assertTrue(result);
        verify(trainerDomainService, times(1)).existsByEmail(anyString());
    }

    @Test
    void givenInValidEmail_whenExistsByEmail_thenReturnFalse() {
        // Given
        String email = "nonexistent@example.com";

        given(trainerDomainService.existsByEmail(anyString())).willReturn(false);

        // When
        boolean result = trainerApplicationService.existsByEmail(email);

        // Then
        assertFalse(result);

        // Verify
        verify(trainerDomainService, times(1)).existsByEmail(anyString());
    }

    @Test
    void givenValidEmail_whenFindByEmail_thenReturnsTrainer() throws EntityNotFoundException {
        // Given
        String email = "johndoe@example.com";
        Trainer trainer = mock(Trainer.class);

        given(trainer.getEmail()).willReturn(email);
        given(trainerDomainService.findByEmail(anyString())).willReturn(trainer);

        Trainer result = trainerApplicationService.findByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(trainer.getEmail(), result.getEmail());
        verify(trainerDomainService, times(1)).findByEmail(anyString());
    }

    @Test
    void givenInValidEmail_whenFindByEmail_thenThrowsEntityNotFoundException() {
        // Given
        String email = "nonexistent@example.com";

        given(trainerDomainService.findByEmail(anyString())).willThrow(new EntityNotFoundException());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> trainerApplicationService.findByEmail(email));

        // Verify
        verify(trainerDomainService, times(1)).findByEmail(anyString());
    }

    @Test
    void givenValidJoinRequest_whenSave_thenReturnTrainer() throws IOException {
        // Given
        JoinRequest request = mock(JoinRequest.class);
        Trainer trainer = mock(Trainer.class);
        SaveTrainerDTO saveTrainerDTO = mock(SaveTrainerDTO.class);

        given(request.getProfile()).willReturn(mock(MockMultipartFile.class));
        given(s3Service.uploadByMultiPartFile(any(MultipartFile.class))).willReturn("fileName");
        given(trainerDomainService.save(saveTrainerDTO)).willReturn(trainer);
        given(trainerMapper.toSaveTrainerDto(anyString(), any(JoinRequest.class))).willReturn(saveTrainerDTO);

        // When
        Trainer result = trainerApplicationService.save(request);

        // Then
        assertNotNull(result);
        verify(s3Service, times(1)).uploadByMultiPartFile(any(MultipartFile.class));
        verify(trainerDomainService, times(1)).save(any(SaveTrainerDTO.class));
    }

    @Test
    void givenValidUpdateInfoRequest_whenUpdateInfo_thenUpdateInfoByEmailCalled() throws EntityNotFoundException {
        // Given
        UpdateInfoRequest request = new UpdateInfoRequest();
        UpdateInfoDTO updateInfoDTO = mock(UpdateInfoDTO.class);
        Trainer trainer = mock(Trainer.class);

        given(trainerDomainService.findByEmail(anyString())).willReturn(trainer);
        given(trainerMapper.toUpdateInfoDto(any(UpdateInfoRequest.class))).willReturn(updateInfoDTO);

        // When
        trainerApplicationService.updateInfo(request);

        // Verify
        verify(trainerDomainService, times(1)).updateInfoByEmail(any(UpdateInfoDTO.class));
    }

    @Test
    void givenValidUpdatePasswordRequest_whenUpdatePassword_thenUpdatePasswordByEmailCalled() {
        // Given
        UpdatePasswordRequest request = mock(UpdatePasswordRequest.class);
        UpdatePasswordDTO updatePasswordDTO = mock(UpdatePasswordDTO.class);

        given(trainerMapper.toUpdatePasswordDto(any(UpdatePasswordRequest.class))).willReturn(updatePasswordDTO);

        // When
        trainerApplicationService.updatePassword(request);

        // Then
        verify(trainerDomainService, times(1)).updatePasswordByEmail(any(UpdatePasswordDTO.class));
    }
}
