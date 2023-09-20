package com.example.deukgeun.trainer.service;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.trainer.domain.dto.SaveLicenseDTO;
import com.example.deukgeun.trainer.domain.dto.SaveTrainerDTO;
import com.example.deukgeun.trainer.domain.dto.UpdateInfoDTO;
import com.example.deukgeun.trainer.domain.dto.UpdatePasswordDTO;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.License;
import com.example.deukgeun.trainer.domain.model.entity.Post;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Address;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Group;
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
    void givenValidTrainerId_whenDeleteById_thenTrainerDeleted() {
        // Given
        Long trainerId = 1L;

        // When
        trainerDomainService.deleteById(trainerId);

        // Verify
        verify(trainerRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void givenValidEmailAndLicenseId_whenDeleteLicenseByEmailAndLicenseId_thenLicenseDeleted() {
        // Given
        String email = "example@example.com";
        Trainer trainer = Trainer.create(
                "name",
                email,
                "password",
                mock(Group.class),
                mock(Address.class),
                Gender.M,
                30000,
                "introduction"
        );
        License license = mock(License.class);
        trainer.setLicenses(license);

        given(trainerRepository.findByEmail(anyString())).willReturn(Optional.of(trainer));

        // When
        trainerDomainService.deleteLicenseByEmailAndLicenseId(email, license.getId());

        // Then
        assertTrue(trainer.getLicenses().isEmpty());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    public void givenValidEmail_whenDeletePostByEmail_thenPostDeleted() {
        // Given
        String email = "test@example.com";
        Post post = mock(Post.class);
        Trainer trainer = mock(Trainer.class);

        trainer.setPost(post);

        given(trainerRepository.findByEmail(anyString())).willReturn(Optional.of(trainer));

        // When
        trainerDomainService.deletePostByEmail(email);

        // Then
        assertNull(trainer.getPost());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    void givenValidEmail_whenExistsByEmail_thenReturnTrue() {
        // Given
        String email = "johndoe@example.com";

        given(trainerRepository.existsByEmail(anyString())).willReturn(true);

        // When
        boolean result = trainerDomainService.existsByEmail(email);

        // Then
        assertTrue(result);
        verify(trainerRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    void givenInValidEmail_whenExistsByEmail_thenReturnFalse() {
        // Given
        String email = "nonexistent@example.com";

        given(trainerRepository.existsByEmail(anyString())).willReturn(false);

        // When
        boolean result = trainerDomainService.existsByEmail(email);

        // Then
        assertFalse(result);
        verify(trainerRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    void givenValidEmail_whenFindByEmail_thenReturnsMatchingTrainer() throws EntityNotFoundException {
        // Given
        Trainer trainer = mock(Trainer.class);

        given(trainer.getEmail()).willReturn("email");
        given(trainerRepository.findByEmail(anyString())).willReturn(Optional.of(trainer));

        // When
        Trainer result = trainerDomainService.findByEmail(trainer.getEmail());

        // Then
        assertNotNull(result);
        assertEquals(trainer.getEmail(), result.getEmail());
        verify(trainerRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void givenInValidEmail_whenFindByEmail_thenThrowsEntityNotFoundException() {
        // Given
        String email = "nonexistent@example.com";
        given(trainerRepository.findByEmail(anyString())).willThrow(new EntityNotFoundException());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> trainerDomainService.findByEmail(email));
        verify(trainerRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void givenValidSaveTrainerDTO_whenSave_thenTrainerSaved() {
        // Given
        SaveTrainerDTO saveTrainerDTO = mock(SaveTrainerDTO.class);
        Trainer savedTrainer = mock(Trainer.class);

        given(saveTrainerDTO.getPassword()).willReturn("password");
        given(trainerRepository.save(any(Trainer.class))).willReturn(savedTrainer);

        // When
        Trainer result = trainerDomainService.save(saveTrainerDTO);

        // Then
        assertNotNull(result);
        assertEquals(savedTrainer.getId(), result.getId());
        assertEquals(savedTrainer.getName(), result.getName());
        assertEquals(savedTrainer.getEmail(), result.getEmail());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    public void givenValidSaveLicenseDTO_whenSaveLicense_thenLicenseSaved() {
        // Given
        Trainer trainer = Trainer.create(
                "name",
                "email",
                "password",
                mock(Group.class),
                mock(Address.class),
                Gender.M,
                30000,
                "introduction"
        );
        SaveLicenseDTO saveLicenseDTO = mock(SaveLicenseDTO.class);

        given(saveLicenseDTO.getEmail()).willReturn("email");
        given(trainerRepository.findByEmail(anyString())).willReturn(Optional.of(trainer));

        // When
        trainerDomainService.saveLicense(saveLicenseDTO);

        // Then
        assertEquals(saveLicenseDTO.getNo() ,trainer.getLicenses().get(0).getLicenseNumber());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    void givenValidUpdateInfoDTO_whenUpdateInfo_thenTrainerUpdated() throws EntityNotFoundException {
        // Given
        Trainer trainer = Trainer.create(
                "name",
                "email",
                "password",
                mock(Group.class),
                mock(Address.class),
                Gender.M,
                30000,
                "introduction"
        );
        UpdateInfoDTO updateInfoDTO = mock(UpdateInfoDTO.class);
        updateInfoDTO.setEmail("updateEmail");

        given(trainerRepository.findByEmail(anyString())).willReturn(Optional.of(trainer));

        // When
        trainerDomainService.updateInfoByEmail(updateInfoDTO);

        // Verify
        assertEquals(updateInfoDTO.getEmail(), trainer.getEmail());
        verify(trainerRepository, times(1)).findByEmail(anyString());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    public void givenValidEmailAndNewPath_whenUpdateProfile_thenProfilePathUpdated() {
        // given
        String newPath = "/path/to/new/profile.jpg";
        String email = "test";
        Trainer trainer = mock(Trainer.class);
        Profile profile = Profile.create("test");

        given(trainerRepository.findByEmail(anyString())).willReturn(Optional.of(trainer));
        given(trainer.getProfile()).willReturn(profile);

        // when
        trainerDomainService.updateProfileByEmail(email, newPath);

        // then
        assertEquals(newPath, trainer.getProfile().getPath());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    void givenValidUpdatePasswordDTO_whenUpdatePassword_thenPasswordIsUpdatedAndSaved() {
        // Given
        String email = "email";
        String password = "password";
        UpdatePasswordDTO updatePasswordDTO = mock(UpdatePasswordDTO.class);
        Trainer trainer = Trainer.create(
                "name",
                email,
                password,
                mock(Group.class),
                mock(Address.class),
                Gender.M,
                30000,
                "introduction"
        );

        given(updatePasswordDTO.getEmail()).willReturn(email);
        given(updatePasswordDTO.getNewPassword()).willReturn("newPassword");
        given(trainerRepository.findByEmail(anyString())).willReturn(Optional.of(trainer));

        // When
        trainerDomainService.updatePasswordByEmail(updatePasswordDTO);

        // Then
        assertNotEquals(password, trainer.getPassword());
        verify(trainerRepository, times(1)).findByEmail(anyString());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    public void givenValidEmailAndHtml_whenUploadPost_thenPostShouldBeUpdatedOrCreated() {
        // Given
        String email = "test@example.com";
        String html = "<p>Hello, this is a test post!</p>";
        Trainer trainer = Trainer.create(
                "name",
                email,
                "password",
                mock(Group.class),
                mock(Address.class),
                Gender.M,
                30000,
                "introduction"
        );

        given(trainerRepository.findByEmail(anyString())).willReturn(Optional.of(trainer));

        // When
        trainerDomainService.uploadPostByEmail(email, html);

        // Then
        assertEquals(html, trainer.getPost().getHtml());
        verify(trainerRepository, times(1)).findByEmail(anyString());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }
}
