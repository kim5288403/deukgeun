package com.example.deukgeun.trainer.service.application;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.service.implement.TrainerApplicationServiceImpl;
import com.example.deukgeun.trainer.domain.model.entity.Trainer;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
import com.example.deukgeun.trainer.domain.service.TrainerDomainService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class TrainerApplicationServiceTest {
    @Mock
    private TrainerDomainService trainerDomainService;
    @InjectMocks
    private TrainerApplicationServiceImpl trainerApplicationService;

    @Test
    void givenExistingTrainerId_whenDeleteById_thenTrainerIsDeleted() {
        // Given
        Long trainerId = 123L;

        // When
        trainerApplicationService.deleteById(trainerId);

        // Verify
        verify(trainerDomainService, times(1)).deleteById(trainerId);
    }

    @Test
    void givenExistingEmail_whenExistsByEmail_thenReturnTrue() {
        // Given
        String email = "johndoe@example.com";

        given(trainerDomainService.existsByEmail(email)).willReturn(true);

        // When
        boolean result = trainerApplicationService.existsByEmail(email);

        // Then
        assertTrue(result);

        // Verify
        verify(trainerDomainService, times(1)).existsByEmail(email);
    }

    @Test
    void givenNonExistingEmail_whenExistsByEmail_thenReturnFalse() {
        // Given
        String email = "nonexistent@example.com";

        given(trainerDomainService.existsByEmail(email)).willReturn(false);

        // When
        boolean result = trainerApplicationService.existsByEmail(email);

        // Then
        assertFalse(result);

        // Verify
        verify(trainerDomainService, times(1)).existsByEmail(email);
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

        given(trainerDomainService.findByEmail(email)).willReturn(trainer);

        Trainer result = trainerApplicationService.findByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(trainer.getEmail(), result.getEmail());

        // Verify
        verify(trainerDomainService, times(1)).findByEmail(email);
    }

    @Test
    void givenNonexistentEmail_whenFindByEmail_thenThrowsEntityNotFoundException() {
        // Given
        String email = "nonexistent@example.com";

        given(trainerDomainService.findByEmail(email)).willThrow(new EntityNotFoundException());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> trainerApplicationService.findByEmail(email));

        // Verify
        verify(trainerDomainService, times(1)).findByEmail(email);
    }

    @Test
    void givenValidGroupStatusAndNonEmptyGroupName_whenIsEmptyGroupName_thenReturnFalse() {
        // Given
        String groupName = "Group A";
        String groupStatus = "Y";

        // When
        boolean result = trainerApplicationService.isEmptyGroupName(groupName, groupStatus);

        // Then
        assertTrue(result);
    }

    @Test
    void givenValidGroupStatusAndEmptyGroupName_whenIsEmptyGroupName_thenReturnTrue() {
        // Given
        String groupName = "";
        String groupStatus = "Y";

        // When
        boolean result = trainerApplicationService.isEmptyGroupName(groupName, groupStatus);

        // Then
        assertFalse(result);
    }

    @Test
    void givenInvalidGroupStatusAndNonEmptyGroupName_whenIsEmptyGroupName_thenReturnTrue() {
        // Given
        String groupName = "Group A";
        String groupStatus = "N";

        // When
        boolean result = trainerApplicationService.isEmptyGroupName(groupName, groupStatus);

        // Then
        assertTrue(result);
    }

    @Test
    void givenInvalidGroupStatusAndEmptyGroupName_whenIsEmptyGroupName_thenReturnTrue() {
        // Given
        String groupName = "";
        String groupStatus = "N";

        // When
        boolean result = trainerApplicationService.isEmptyGroupName(groupName, groupStatus);

        // Then
        assertTrue(result);
    }

    @Test
    void givenJoinRequest_whenSave_thenTrainerIsSavedAndReturned() {
        // Given
        JoinRequest request = new JoinRequest();
        request.setName("John Doe");
        request.setEmail("johndoe@example.com");
        request.setPassword("password123");

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

        given(trainerDomainService.save(request)).willReturn(savedTrainer);

        // When
        Trainer result = trainerApplicationService.save(request);

        // Then
        assertNotNull(result);
        assertEquals(savedTrainer.getId(), result.getId());
        assertEquals(savedTrainer.getName(), result.getName());
        assertEquals(savedTrainer.getEmail(), result.getEmail());

        // Verify
        verify(trainerDomainService, times(1)).save(request);
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

        given(trainerDomainService.findByEmail(request.getEmail())).willReturn(trainer);

        // When
        trainerApplicationService.updateInfo(request);

        // Verify
        verify(trainerDomainService, times(1)).updateInfo(request);
    }

    @Test
    void givenValidUpdatePasswordRequest_whenUpdatePassword_thenPasswordIsUpdatedAndSaved() {
        // Given
        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setEmail("johndoe@example.com");
        request.setNewPassword("newPassword123");

        // When
        trainerApplicationService.updatePassword(request);

        // Then
        verify(trainerDomainService, times(1)).updatePassword(request);
    }
}
