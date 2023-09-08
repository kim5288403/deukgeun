package com.example.deukgeun.trainer.service;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResponse;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.License;
import com.example.deukgeun.trainer.domain.model.entity.Post;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Address;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Group;
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
import java.util.List;
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
                new Group(
                        GroupStatus.Y,
                        "test"
                ),
                new Address(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                ),
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
    public void givenTrainerWithEmail_whenDeletePost_thenPostShouldBeDeleted() {
        // Given (주어진 상황)
        String email = "test@example.com";
        Long id = 123L;
        Post post = new Post(123L, "Test");
        Trainer trainer = new Trainer(
                id,
                "test",
                email,
                "test",
                new Group(
                        GroupStatus.Y,
                        "test"
                ),
                new Address(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                ),
                Gender.M,
                3000,
                "test"
        );

        trainer.setPost(post);

        given(trainerRepository.findByEmail(email)).willReturn(Optional.of(trainer));

        // When (실행)
        trainerDomainService.deletePost(email);

        // Then (결과 확인)
        verify(trainerRepository, times(1)).save(trainer);
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
        Trainer trainer = new Trainer(
                123L,
                "test",
                "test",
                "test",
                new Group(
                        GroupStatus.Y,
                        "test"
                ),
                new Address(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                ),
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

        Trainer savedTrainer = new Trainer(
                123L,
                "test",
                "test",
                "test",
                new Group(
                        GroupStatus.Y,
                        "test"
                ),
                new Address(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                ),
                Gender.M,
                3000,
                "test"
        );

        given(trainerRepository.save(any(Trainer.class))).willReturn(savedTrainer);

        // When
        Trainer result = trainerDomainService.save(request, "fileName");

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
        LicenseResponse.Result licenseResult = new LicenseResponse.Result(true, "CertificateName", "123456");

        Trainer trainer = new Trainer(
                123L,
                "test",
                email,
                "test",
                new Group(
                        GroupStatus.Y,
                        "test"
                ),
                new Address(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                ),
                Gender.M,
                3000,
                "test"
        );

        given(trainerRepository.findByEmail(email)).willReturn(Optional.of(trainer));

        // When
        trainerDomainService.saveLicense(email, licenseResult);

        // Then
        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    void givenValidUpdateInfoRequest_whenUpdateInfo_thenTrainerIsUpdatedAndSaved() throws EntityNotFoundException {
        // Given
        UpdateInfoRequest request = new UpdateInfoRequest();
        request.setEmail("johndoe@example.com");

        Trainer trainer = new Trainer(
                123L,
                "test",
                "test",
                "test",
                new Group(
                        GroupStatus.Y,
                        "test"
                ),
                new Address(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                ),
                Gender.M,
                3000,
                "test"
        );

        given(trainerRepository.findByEmail(request.getEmail())).willReturn(Optional.of(trainer));

        // When
        trainerDomainService.updateInfoByEmail(request);

        // Verify
        verify(trainerRepository, times(1)).findByEmail(request.getEmail());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    public void givenTrainerAndNewPath_whenUpdateProfile_thenProfilePathUpdated() {
        // given
        String newPath = "/path/to/new/profile.jpg";
        String email = "test";
        Trainer trainer = new Trainer(
                123L,
                "test",
                email,
                "test",
                new Group(
                        GroupStatus.Y,
                        "test"
                ),
                new Address(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                ),
                Gender.M,
                3000,
                "test",
                mock(List.class),
                new Profile(1234L,  "test"),
                mock(Post.class)
        );

        given(trainerRepository.findByEmail(email)).willReturn(Optional.of(trainer));

        // when
        trainerDomainService.updateProfileByEmail(email, newPath);

        // then
        verify(trainerRepository, times(1)).save(trainer);
        assertEquals(newPath, trainer.getProfile().getPath());
    }

    @Test
    void givenValidUpdatePasswordRequest_whenUpdatePassword_thenPasswordIsUpdatedAndSaved() {
        // Given
        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setEmail("johndoe@example.com");
        request.setNewPassword("newPassword123");

        Trainer trainer = new Trainer(
                123L,
                "test",
                "test",
                "test",
                new Group(
                        GroupStatus.Y,
                        "test"
                ),
                new Address(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                ),
                Gender.M,
                3000,
                "test"
        );

        given(trainerRepository.findByEmail(request.getEmail())).willReturn(Optional.of(trainer));

        // When
        trainerDomainService.updatePasswordByEmail(request);

        // Then
        verify(trainerRepository, times(1)).findByEmail(request.getEmail());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    public void givenTrainerWithEmailAndHtml_whenUploadPost_thenPostShouldBeUpdatedOrCreated() {
        // Given
        String email = "test@example.com";
        String html = "<p>Hello, this is a test post!</p>";
        Trainer trainer = new Trainer(
                123L,
                "test",
                email,
                "test",
                new Group(
                        GroupStatus.Y,
                        "test"
                ),
                new Address(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                ),
                Gender.M,
                3000,
                "test"
        );

        given(trainerRepository.findByEmail(email)).willReturn(Optional.of(trainer));

        // When
        trainerDomainService.uploadPostByEmail(email, html);

        // Then
        verify(trainerRepository, times(1)).findByEmail(email);
        verify(trainerRepository, times(1)).save(trainer);
    }
}
