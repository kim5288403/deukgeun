package com.example.deukgeun.trainer.service;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResultResponse;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.License;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
import com.example.deukgeun.trainer.domain.repository.TrainerRepository;
import com.example.deukgeun.trainer.domain.service.implement.TrainerDomainServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
class TrainerDomainServiceTest {
    @InjectMocks
    private TrainerDomainServiceImpl trainerDomainService;
    @Mock
    private TrainerRepository trainerRepository;

    @Test
    void givenExistingTrainerId_whenDeleteById_thenTrainerIsDeleted() {
        // Given
        Long trainerId = 123L;

        // When
        trainerDomainService.deleteById(trainerId);

        // Verify
        verify(trainerRepository, times(1)).deleteById(trainerId);
    }

    @Test
    public void givenTrainerWithEmailAndLicense_whenDeleteLicenseByLicenseId_thenLicenseShouldBeRemoved() {
        // Given
        String email = "example@example.com";
        Long licenseId = 123L;

        // Trainer 객체와 라이선스 객체를 생성합니다.
        Trainer trainer = new Trainer(
                123L,
                "test",
                email,
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

        License license = new License(123L, "test", "test", trainer.getId());
        trainer.getLicenses().add(license);

        given(trainerRepository.findByEmail(email)).willReturn(Optional.of(trainer));

        // When
        trainerDomainService.deleteLicenseByLicenseId(email, licenseId);

        // Then
        assertTrue(trainer.getLicenses().isEmpty());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    void givenExistingEmail_whenExistsByEmail_thenReturnTrue() {
        // Given
        String email = "johndoe@example.com";

        given(trainerRepository.existsByEmail(email)).willReturn(true);

        // When
        boolean result = trainerDomainService.existsByEmail(email);

        // Then
        assertTrue(result);

        // Verify
        verify(trainerRepository, times(1)).existsByEmail(email);
    }

    @Test
    void givenNonExistingEmail_whenExistsByEmail_thenReturnFalse() {
        // Given
        String email = "nonexistent@example.com";

        given(trainerRepository.existsByEmail(email)).willReturn(false);

        // When
        boolean result = trainerDomainService.existsByEmail(email);

        // Then
        assertFalse(result);

        // Verify
        verify(trainerRepository, times(1)).existsByEmail(email);
    }

    @Test
    void givenExistingEmail_whenFindByEmail_thenReturnsMatchingTrainer() throws EntityNotFoundException {
        // Given
        String email = "johndoe@example.com";
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

        given(trainerRepository.findByEmail(email)).willReturn(Optional.of(trainer));

        Trainer result = trainerDomainService.findByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(trainer.getEmail(), result.getEmail());

        // Verify
        verify(trainerRepository, times(1)).findByEmail(email);
    }

    @Test
    void givenNonexistentEmail_whenFindByEmail_thenThrowsEntityNotFoundException() {
        // Given
        String email = "nonexistent@example.com";

        given(trainerRepository.findByEmail(email)).willThrow(new EntityNotFoundException());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> trainerDomainService.findByEmail(email));

        // Verify
        verify(trainerRepository, times(1)).findByEmail(email);
    }

    @Test
    void givenJoinRequest_whenSave_thenTrainerIsSavedAndReturned() {
        // Given
        JoinRequest request = new JoinRequest();
        request.setName("test");
        request.setEmail("test");
        request.setPassword("test");
        request.setGroupStatus(GroupStatus.N);
        request.setGroupName("test");
        request.setPostcode("test");
        request.setJibunAddress("test");
        request.setRoadAddress("test");
        request.setDetailAddress("test");
        request.setExtraAddress("test");
        request.setGender(Gender.M);
        request.setPrice(3000);
        request.setIntroduction("test");

        Trainer savedTrainer = new Trainer (
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

        given(trainerRepository.save(any(Trainer.class))).willReturn(savedTrainer);

        // When
        Trainer result = trainerDomainService.save(request);

        // Then
        assertNotNull(result);
        assertEquals(savedTrainer.getId(), result.getId());
        assertEquals(savedTrainer.getName(), result.getName());
        assertEquals(savedTrainer.getEmail(), result.getEmail());

        // Verify
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    public void givenTrainerWithEmailAndLicenseResult_whenSaveLicense_thenLicenseShouldBeAddedToTrainer() {
        // Given
        String email = "example@example.com";
        LicenseResultResponse licenseResult = new LicenseResultResponse(true,"CertificateName", "123456");

        Trainer trainer = new Trainer (
                123L,
                "test",
                email,
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

        given(trainerRepository.findByEmail(email)).willReturn(Optional.of(trainer));

        // When
        Trainer savedTrainer = trainerDomainService.saveLicense(email, licenseResult);

        // Then
        assertEquals(1, savedTrainer.getLicenses().size());
        License savedLicense = savedTrainer.getLicenses().get(0);
        assertEquals("CertificateName", savedLicense.getCertificateName());
        assertEquals("123456", savedLicense.getLicenseNumber());
        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    void givenValidUpdateInfoRequest_whenUpdateInfo_thenTrainerIsUpdatedAndSaved() throws EntityNotFoundException {
        // Given
        UpdateInfoRequest request = new UpdateInfoRequest();
        request.setEmail("johndoe@example.com");

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

        given(trainerRepository.findByEmail(request.getEmail())).willReturn(Optional.of(trainer));

        // When
        trainerDomainService.updateInfo(request);

        // Verify
        verify(trainerRepository, times(1)).findByEmail(request.getEmail());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    void givenValidUpdatePasswordRequest_whenUpdatePassword_thenPasswordIsUpdatedAndSaved() {
        // Given
        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setEmail("johndoe@example.com");
        request.setNewPassword("newPassword123");

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

        given(trainerRepository.findByEmail(request.getEmail())).willReturn(Optional.of(trainer));

        // When
        trainerDomainService.updatePassword(request);

        // Then
        verify(trainerRepository, times(1)).findByEmail(request.getEmail());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }
}
