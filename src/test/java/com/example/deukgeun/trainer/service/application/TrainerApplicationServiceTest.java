package com.example.deukgeun.trainer.service.application;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.global.util.MultipartFileUtil;
import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.PostRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResponse;
import com.example.deukgeun.trainer.application.dto.response.ProfileResponse;
import com.example.deukgeun.trainer.application.service.implement.TrainerApplicationServiceImpl;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.License;
import com.example.deukgeun.trainer.domain.model.entity.Post;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Address;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Group;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
import com.example.deukgeun.trainer.domain.service.TrainerDomainService;
import com.example.deukgeun.trainer.infrastructure.s3.S3Service;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
class TrainerApplicationServiceTest {
    @Mock
    private TrainerDomainService trainerDomainService;
    @Mock
    private S3Service s3Service;
    @InjectMocks
    private TrainerApplicationServiceImpl trainerApplicationService;

    @BeforeAll
    public static void setUp() {
        mockStatic(MultipartFileUtil.class);
    }

    @Test
    void givenExistingTrainerId_whenDelete_thenTrainerIsDeleted() throws IOException {
        // Given
        Long trainerId = 123L;
        String email = "test";
        Trainer trainer = new Trainer(
                trainerId,
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
                mock(Profile.class),
                mock(Post.class)
        );

        given(trainerDomainService.findByEmail(email)).willReturn(trainer);

        // When
        trainerApplicationService.delete(email);

        // Verify
        verify(trainerDomainService, times(1)).deleteById(trainerId);
        verify(s3Service, times(1)).delete(trainer.getProfile().getPath());
    }

    @Test
    public void givenTrainerWithEmailAndLicenseId_whenDeleteLicenseByLicenseId_thenLicenseShouldBeDeleted() {
        // Given
        String email = "test@example.com";
        Long licenseId = 12345L;

        // When
        trainerApplicationService.deleteLicenseByLicenseId(email, licenseId);

        // Then
        verify(trainerDomainService, times(1)).deleteLicenseByLicenseId(email, licenseId);
    }

    @Test
    public void givenTrainerWithEmail_whenDeletePost_thenPostShouldBeDeleted() {
        // Given
        String email = "test@example.com";
        String src = "testSrc";

        // When
        trainerApplicationService.deletePost(email, src);

        // Then
        verify(s3Service, times(1)).delete(src);
        verify(trainerDomainService, times(1)).deletePost(email);
    }

    @Test
    public void givenImageSrc_whenDeleteImageToS3_thenImageShouldBeDeleted() throws IOException {
        // Given
        String src = "http://example.com/images/image.jpg";

        // When
        trainerApplicationService.deleteImageToS3(src);

        // Then
        verify(s3Service, times(1)).delete(src);
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
    public void givenEmail_whenGetProfileByEmail_thenProfileResponseWithPath() {
        // Given
        String email = "example@example.com";
        String path = "/path/to/profile";
        Trainer trainer = mock(Trainer.class);
        Profile profile = mock(Profile.class);

        given(trainerApplicationService.findByEmail(email)).willReturn(trainer);
        given(trainer.getProfile()).willReturn(profile);
        given(profile.getPath()).willReturn(path);

        // When
        ProfileResponse response = trainerApplicationService.getProfileByEmail(email);

        // Then
        assertNotNull(response);
        assertEquals(path, response.getPath());
        verify(trainerDomainService, times(1)).findByEmail(email);
    }

    @Test
    public void givenTrainerWithLicenses_whenGetLicensesById_thenReturnLicenseResponseList() {
        // Given
        Long id = 1L;

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
        License license1 = new License(123L, "test1", "test1", id);
        License license2 = new License(124L, "test2", "test2", id);
        trainer.getLicenses().add(license1);
        trainer.getLicenses().add(license2);

        given(trainerDomainService.findById(id)).willReturn(trainer);

        // When
        List<LicenseResponse.List> result = trainerApplicationService.getLicensesById(id);

        // Then
        assertEquals(2, result.size());
        assertEquals(123L, result.get(0).getLicenseId());
        assertEquals("test1", result.get(0).getCertificateName());
        assertEquals(124L, result.get(1).getLicenseId());
        assertEquals("test2", result.get(1).getCertificateName());
        verify(trainerDomainService, times(1)).findById(id);
    }

    @Test
    public void givenTrainerWithLicenses_whenGetLicensesByEmail_thenReturnLicenseResponseList() {
        // Given
        Long id = 1L;
        String email = "email";
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
        License license1 = new License(123L, "test1", "test1", id);
        License license2 = new License(124L, "test2", "test2", id);
        trainer.getLicenses().add(license1);
        trainer.getLicenses().add(license2);

        given(trainerDomainService.findByEmail(email)).willReturn(trainer);

        // When
        List<LicenseResponse.List> result = trainerApplicationService.getLicensesByEmail(email);

        // Then
        assertEquals(2, result.size());
        assertEquals(123L, result.get(0).getLicenseId());
        assertEquals("test1", result.get(0).getCertificateName());
        assertEquals(124L, result.get(1).getLicenseId());
        assertEquals("test2", result.get(1).getCertificateName());
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
    void givenJoinRequest_whenSave_thenTrainerIsSavedAndReturned() throws IOException {
        // Given
        JoinRequest request = mock(JoinRequest.class);
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

        given(request.getProfile()).willReturn(mock(MockMultipartFile.class));
        given(s3Service.upload(any(MultipartFile.class))).willReturn("test");
        given(trainerDomainService.save(any(JoinRequest.class), anyString())).willReturn(savedTrainer);

        // When
        Trainer result = trainerApplicationService.save(request);

        // Then
        assertNotNull(result);
        assertEquals(savedTrainer.getId(), result.getId());
        assertEquals(savedTrainer.getName(), result.getName());
        assertEquals(savedTrainer.getEmail(), result.getEmail());
        verify(trainerDomainService, times(1)).save(any(JoinRequest.class), anyString());
    }

    @Test
    public void givenEmailAndLicenseResult_whenSaveLicense_thenTrainerShouldBeReturned() {
        // Given
        String email = "test@example.com";
        LicenseResponse.Result licenseResult = new LicenseResponse.Result();

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
        given(trainerDomainService.saveLicense(email, licenseResult)).willReturn(trainer);

        // When (실행)
        Trainer resultTrainer = trainerApplicationService.saveLicense(email, licenseResult);

        // Then (결과 확인)
        assertEquals(trainer, resultTrainer);
    }

    @Test
    public void givenValidRequestAndResponse_whenSaveImageToS3_thenReturnLinkInMap() throws Exception {
        // Given
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpServletResponse responseMock = mock(HttpServletResponse.class);
        Part filePartMock = mock(Part.class);

        given(requestMock.getPart("file")).willReturn(filePartMock);
        given(filePartMock.getContentType()).willReturn("image/jpeg"); // 적절한 MIME 타입 설정

        // When
        Map<Object, Object> responseData = trainerApplicationService.saveImageToS3(requestMock, responseMock);

        // Then
        assertNotNull(responseData);
        assertEquals(1, responseData.size());
        assertTrue(responseData.containsKey("link"));
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

        given(trainerDomainService.findByEmail(request.getEmail())).willReturn(trainer);

        // When
        trainerApplicationService.updateInfo(request);

        // Verify
        verify(trainerDomainService, times(1)).updateInfo(request);
    }

    @Test
    public void givenEmailAndProfile_whenUpdateProfile_thenProfileShouldBeUpdated() throws Exception {
        // Given
        String email = "test@example.com";
        String originalFilename = "profile.jpg";
        String expectedFileName = "/path/to/profile/files/your_generated_uuid.jpg";

        MultipartFile profileMock = mock(MultipartFile.class);
        Trainer trainerMock = mock(Trainer.class);

        given(profileMock.getOriginalFilename()).willReturn(originalFilename);
        given(profileMock.getContentType()).willReturn("image/jpeg");
        given(trainerDomainService.findByEmail(email)).willReturn(trainerMock);
        given(trainerMock.getProfile()).willReturn(mock(Profile.class));
        given(trainerMock.getProfile().getPath()).willReturn(originalFilename);
        given(s3Service.upload(profileMock)).willReturn(expectedFileName);

        // When
        trainerApplicationService.updateProfile(email, profileMock);

        // Then
        verify(trainerDomainService, times(1)).findByEmail(email);
        verify(s3Service, times(1)).delete(trainerMock.getProfile().getPath());
        verify(s3Service, times(1)).upload(profileMock);
        verify(trainerDomainService, times(1)).updateProfile(trainerMock, expectedFileName);
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

    @Test
    public void givenEmailAndPostRequest_whenUploadPost_thenPostShouldBeUploadedWithEscapedHtml() {
        // Given
        String email = "test@example.com";
        String content = "<script>alert('Hello, World!');</script>";

        PostRequest postRequest = new PostRequest();
        postRequest.setContent(content);

        // When
        trainerApplicationService.uploadPost(email, postRequest);

        // Then
        verify(trainerDomainService, times(1)).uploadPost(email, HtmlUtils.htmlEscape(content));
    }
}
