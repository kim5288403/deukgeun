package com.example.deukgeun.trainer.service;

import com.example.deukgeun.commom.exception.PasswordMismatchException;
import com.example.deukgeun.commom.request.LoginRequest;
import com.example.deukgeun.commom.service.implement.TokenServiceImpl;
import com.example.deukgeun.trainer.entity.Trainer;
import com.example.deukgeun.trainer.repository.ProfileRepository;
import com.example.deukgeun.trainer.repository.TrainerRepository;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.response.TrainerResponse;
import com.example.deukgeun.trainer.service.implement.TrainerServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
    private ProfileRepository profileRepository;
    @Mock
    private TokenServiceImpl tokenService;
    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Test
    void givenMatchingPassword_whenIsPasswordMatches_thenNoExceptionThrown() throws EntityNotFoundException {
        // Given
        String email = "example@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword123";

        LoginRequest request = new LoginRequest(email, password);
        Trainer trainer = Trainer
                .builder()
                .email(email)
                .password(encodedPassword)
                .build();

        given(passwordEncoder.matches(password, encodedPassword)).willReturn(true);

        // When & Then
        assertDoesNotThrow(() -> trainerService.isPasswordMatches(request.getPassword(), trainer));

        // Verify
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
    }

    @Test
    void givenMismatchingPassword_whenIsPasswordMatches_thenPasswordMismatchExceptionThrown() throws EntityNotFoundException {
        // Given
        String email = "example@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword123";

        LoginRequest request = new LoginRequest(email, password);
        Trainer trainer = Trainer
                .builder()
                .email(email)
                .password(encodedPassword)
                .build();

        given(passwordEncoder.matches(password, encodedPassword)).willReturn(false);

        // When & Then
        assertThrows(PasswordMismatchException.class, () -> trainerService.isPasswordMatches(request.getPassword(), trainer));

        // Verify
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
    }

    @Test
    void givenKeywordAndPage_whenGetList_thenReturnsMatchingUsers() {
        // Given
        String keyword = "john";
        int currentPage = 0;

        String likeKeyword = "%" + keyword + "%";
        PageRequest pageable = PageRequest.of(currentPage, 10);

        TrainerResponse.TrainerListResponse trainer1 = new TrainerResponse.TrainerListResponse();
        trainer1.setId(1L);
        trainer1.setName("name1");
        trainer1.setPath("path1");

        TrainerResponse.TrainerListResponse trainer2 = new TrainerResponse.TrainerListResponse();
        trainer2.setId(2L);
        trainer2.setName("name2");
        trainer2.setPath("path2");

        List<TrainerResponse.TrainerListResponse> trainerList = new ArrayList<>();
        trainerList.add(trainer1);
        trainerList.add(trainer2);
        Page<TrainerResponse.TrainerListResponse> userPage = new PageImpl<>(trainerList, pageable, trainerList.size());

        given(profileRepository.findByTrainerLikeKeyword(likeKeyword, pageable)).willReturn(userPage);

        // When
        Page<TrainerResponse.TrainerListResponse> result = trainerService.getList(keyword, currentPage);

        // Then
        assertNotNull(result);
        assertEquals(trainerList.size(), result.getContent().size());

        // Verify
        verify(profileRepository, times(1)).findByTrainerLikeKeyword(likeKeyword, pageable);
    }

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

        given(tokenService.getUserPk(authToken)).willReturn(email);
        given(trainerRepository.findByEmail(email)).willReturn(Optional.ofNullable(trainer));

        // When
        Trainer result = trainerService.getByAuthToken(authToken);

        // Then
        assertNotNull(result);
        assertNotNull(trainer);
        assertEquals(trainer.getEmail(), result.getEmail());

        // Verify
        verify(tokenService, times(1)).getUserPk(authToken);
        verify(trainerRepository, times(1)).findByEmail(email);
    }

    @Test
    void givenInvalidAuthToken_whenGetByAuthToken_thenThrowsEntityNotFoundException() throws EntityNotFoundException {
        // Given
        String authToken = "invalidAuthToken";

        given(tokenService.getUserPk(authToken)).willReturn(null);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> trainerService.getByAuthToken(authToken));

        // Verify
        verify(tokenService, times(1)).getUserPk(authToken);
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

        given(tokenService.getUserPk(authToken)).willReturn(email);
        given(trainerRepository.findByEmail(email)).willReturn(Optional.ofNullable(trainer));

        // When
        Long result = trainerService.getTrainerId(authToken);

        // Then
        assertEquals(userId, result);

        // Verify
        verify(tokenService, times(1)).getUserPk(authToken);
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
