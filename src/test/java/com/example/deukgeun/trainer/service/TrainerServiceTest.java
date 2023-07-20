package com.example.deukgeun.trainer.service;

import com.example.deukgeun.authToken.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.domain.entity.Trainer;
import com.example.deukgeun.trainer.domain.repository.TrainerRepository;
import com.example.deukgeun.trainer.infrastructure.persistence.TrainerServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
class TrainerServiceTest {
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthTokenApplicationServiceImpl authTokenApplicationService;
    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Test
    void givenJoinRequest_whenSave_thenTrainerIsSavedAndReturned() {
        // Given
        JoinRequest request = new JoinRequest();
        request.setName("John Doe");
        request.setEmail("johndoe@example.com");
        request.setPassword("password123");

        Trainer savedTrainer = Trainer
                .builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();

        given(passwordEncoder.encode(request.getPassword())).willReturn("encodedPassword123");
        given(trainerRepository.save(any(Trainer.class))).willReturn(savedTrainer);

        // When
        Trainer result = trainerService.save(request);

        // Then
        assertNotNull(result);
        assertEquals(savedTrainer.getId(), result.getId());
        assertEquals(savedTrainer.getName(), result.getName());
        assertEquals(savedTrainer.getEmail(), result.getEmail());

        // Verify
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    void givenExistingEmail_whenGetByEmail_thenReturnsMatchingTrainer() throws EntityNotFoundException {
        // Given
        String email = "johndoe@example.com";
        Trainer trainer = Trainer
                .builder()
                .email(email)
                .build();

        given(trainerRepository.findByEmail(email)).willReturn(Optional.of(trainer));

        Trainer result = trainerService.getByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(trainer.getEmail(), result.getEmail());

        // Verify
        verify(trainerRepository, times(1)).findByEmail(email);
    }

    @Test
    void givenNonexistentEmail_whenGetByEmail_thenThrowsEntityNotFoundException() {
        // Given
        String email = "nonexistent@example.com";

        given(trainerRepository.findByEmail(email)).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> trainerService.getByEmail(email));

        // Verify
        verify(trainerRepository, times(1)).findByEmail(email);
    }

    @Test
    void givenValidAuthToken_whenGetByAuthToken_thenReturnsMatchingTrainer() throws EntityNotFoundException {
        // Given
        String authToken = "validAuthToken";
        String email = "johndoe@example.com";
        Trainer trainer = Trainer
                .builder()
                .email(email)
                .build();

        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);
        given(trainerRepository.findByEmail(email)).willReturn(Optional.ofNullable(trainer));

        // When
        Trainer result = trainerService.getByAuthToken(authToken);

        // Then
        assertNotNull(result);
        assertNotNull(trainer);
        assertEquals(trainer.getEmail(), result.getEmail());

        // Verify
        verify(authTokenApplicationService, times(1)).getUserPk(authToken);
        verify(trainerRepository, times(1)).findByEmail(email);
    }

    @Test
    void givenInvalidAuthToken_whenGetByAuthToken_thenThrowsEntityNotFoundException() throws EntityNotFoundException {
        // Given
        String authToken = "invalidAuthToken";

        given(authTokenApplicationService.getUserPk(authToken)).willReturn(null);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> trainerService.getByAuthToken(authToken));

        // Verify
        verify(authTokenApplicationService, times(1)).getUserPk(authToken);
    }

    @Test
    void givenValidUpdateInfoRequest_whenUpdateInfo_thenTrainerIsUpdatedAndSaved() throws EntityNotFoundException {
        // Given
        UpdateInfoRequest request = new UpdateInfoRequest();
        request.setEmail("johndoe@example.com");

        Trainer foundTrainer = Trainer
                .builder()
                .email(request.getEmail())
                .build();

        given(trainerRepository.findByEmail(request.getEmail())).willReturn(Optional.of(foundTrainer));
        given(trainerRepository.save(foundTrainer)).willReturn(foundTrainer);

        // When
        trainerService.updateInfo(request);

        // Verify
        verify(trainerRepository, times(1)).findByEmail(request.getEmail());
        verify(trainerRepository, times(1)).save(foundTrainer);
    }

    @Test
    void givenInvalidUpdateInfoRequest_whenUpdateInfo_thenThrowsEntityNotFoundException() {
        // Given
        UpdateInfoRequest request = new UpdateInfoRequest();
        request.setEmail("nonexistent@example.com");

        given(trainerRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> trainerService.updateInfo(request));

        // Verify
        verify(trainerRepository, times(1)).findByEmail(request.getEmail());
        verify(trainerRepository, never()).save(any(Trainer.class));
    }

    @Test
    void givenValidUpdatePasswordRequest_whenUpdatePassword_thenPasswordIsUpdatedAndSaved() {
        // Given
        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setEmail("johndoe@example.com");
        request.setNewPassword("newPassword123");

        Trainer foundTrainer = Trainer
                .builder()
                .email(request.getEmail())
                .build();

        String encodedNewPassword = "encodedNewPassword123";

        given(trainerRepository.findByEmail(request.getEmail())).willReturn(Optional.of(foundTrainer));
        given(passwordEncoder.encode(request.getNewPassword())).willReturn(encodedNewPassword);
        given(trainerRepository.save(foundTrainer)).willReturn(foundTrainer);

        // When
        trainerService.updatePassword(request);

        // Then
        assertEquals(encodedNewPassword, foundTrainer.getPassword());

        // Verify
        verify(trainerRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).encode(request.getNewPassword());
        verify(trainerRepository, times(1)).save(foundTrainer);
    }

    @Test
    void givenInvalidUpdatePasswordRequest_whenUpdatePassword_thenTrainerIsNotFound() {
        // Given
        UpdatePasswordRequest request = new UpdatePasswordRequest();
        String encodedNewPassword = "encodedNewPassword123";
        request.setEmail("nonexistent@example.com");
        request.setNewPassword("newPassword123");

        given(trainerRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());
        given(passwordEncoder.encode(request.getNewPassword())).willReturn(encodedNewPassword);

        // When & Then
        assertThrows(AssertionError.class, () -> trainerService.updatePassword(request));

        // Verify
        verify(trainerRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).encode(request.getNewPassword());
    }

    @Test
    void givenExistingTrainerId_whenWithdrawal_thenTrainerIsDeleted() {
        // Given
        Long trainerId = 123L;

        // When
        trainerService.withdrawal(trainerId);

        // Verify
        verify(trainerRepository, times(1)).deleteById(trainerId);
    }

    @Test
    void givenValidAuthToken_whenGetUserId_thenReturnUserId() throws EntityNotFoundException {
        // Given
        String authToken = "validAuthToken";
        String email = "johndoe@example.com";
        Long userId = 123L;
        Trainer trainer = Trainer
                .builder()
                .id(userId)
                .email(email)
                .build();

        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);
        given(trainerRepository.findByEmail(email)).willReturn(Optional.ofNullable(trainer));

        // When
        Long result = trainerService.getTrainerId(authToken);

        // Then
        assertEquals(userId, result);

        // Verify
        verify(authTokenApplicationService, times(1)).getUserPk(authToken);
        verify(trainerRepository, times(1)).findByEmail(email);
    }

    @Test
    void givenExistingEmail_whenIsDuplicateEmail_thenReturnTrue() {
        // Given
        String email = "johndoe@example.com";

        given(trainerRepository.existsByEmail(email)).willReturn(true);

        // When
        boolean result = trainerService.isDuplicateEmail(email);

        // Then
        assertTrue(result);

        // Verify
        verify(trainerRepository, times(1)).existsByEmail(email);
    }

    @Test
    void givenNonExistingEmail_whenIsDuplicateEmail_thenReturnFalse() {
        // Given
        String email = "nonexistent@example.com";

        given(trainerRepository.existsByEmail(email)).willReturn(false);

        // When
        boolean result = trainerService.isDuplicateEmail(email);

        // Then
        assertFalse(result);

        // Verify
        verify(trainerRepository, times(1)).existsByEmail(email);
    }

    @Test
    void givenValidGroupStatusAndNonEmptyGroupName_whenIsEmptyGroupName_thenReturnFalse() {
        // Given
        String groupName = "Group A";
        String groupStatus = "Y";

        // When
        boolean result = trainerService.isEmptyGroupName(groupName, groupStatus);

        // Then
        assertTrue(result);
    }

    @Test
    void givenValidGroupStatusAndEmptyGroupName_whenIsEmptyGroupName_thenReturnTrue() {
        // Given
        String groupName = "";
        String groupStatus = "Y";

        // When
        boolean result = trainerService.isEmptyGroupName(groupName, groupStatus);

        // Then
        assertFalse(result);
    }

    @Test
    void givenInvalidGroupStatusAndNonEmptyGroupName_whenIsEmptyGroupName_thenReturnTrue() {
        // Given
        String groupName = "Group A";
        String groupStatus = "N";

        // When
        boolean result = trainerService.isEmptyGroupName(groupName, groupStatus);

        // Then
        assertTrue(result);
    }

    @Test
    void givenInvalidGroupStatusAndEmptyGroupName_whenIsEmptyGroupName_thenReturnTrue() {
        // Given
        String groupName = "";
        String groupStatus = "N";

        // When
        boolean result = trainerService.isEmptyGroupName(groupName, groupStatus);

        // Then
        assertTrue(result);
    }
}
