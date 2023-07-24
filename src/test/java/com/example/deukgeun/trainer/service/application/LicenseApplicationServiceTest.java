package com.example.deukgeun.trainer.service.application;

import com.example.deukgeun.trainer.application.dto.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseListResponse;
import com.example.deukgeun.trainer.application.dto.response.LicenseResultResponse;
import com.example.deukgeun.trainer.application.service.implement.LicenseApplicationServiceImpl;
import com.example.deukgeun.trainer.domain.model.entity.License;
import com.example.deukgeun.trainer.domain.service.LicenseDomainService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class LicenseApplicationServiceTest {
    @InjectMocks
    private LicenseApplicationServiceImpl licenseApplicationService;
    @Mock
    private LicenseDomainService licenseDomainService;

    @Value("${trainer.license.api.key}")
    private String licenseApiKey;
    @Value("${trainer.license.api.uri}")
    private String licenseApiUri;

    @Test
    public void givenValidLicenseResultResponse_whenCheckingLicense_thenNoExceptionThrown() {
        // Given
        LicenseResultResponse result = new LicenseResultResponse();
        result.setResult(true);
        result.setCertificatename("test");
        result.setNo("12345");

        // When & Then
        assertDoesNotThrow(() -> licenseApplicationService.checkLicense(result));
    }

    @Test
    public void givenInvalidLicenseResultResponse_whenCheckingLicense_thenIllegalArgumentExceptionThrown() {
        // Given
        LicenseResultResponse result = new LicenseResultResponse();
        result.setResult(false);
        result.setCertificatename("test");
        result.setNo("12345");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> licenseApplicationService.checkLicense(result));
    }

    @Test
    public void givenLicenseId_whenDeleteById_thenRepositoryCalled() {
        // Given
        Long licenseId = 1L;

        // When
        licenseApplicationService.deleteById(licenseId);

        // Then
        verify(licenseDomainService, times(1)).deleteById(licenseId);
    }

    @Test
    public void givenValidTrainerId_whenFindByTrainerId_thenReturnLicenseListResponse() {
        // Given
        Long trainerId = 123L;
        LicenseListResponse licenseListResponseA = new LicenseListResponse
                (
                        1L,
                        "testA",
                        "test",
                        LocalDateTime.now()
                );
        LicenseListResponse licenseListResponseB = new LicenseListResponse
                (
                        1L,
                        "testB",
                        "test",
                        LocalDateTime.now()
                );

        List<LicenseListResponse> licenses = Arrays.asList(licenseListResponseA, licenseListResponseB);
        given(licenseDomainService.findByTrainerId(trainerId)).willReturn(licenses);

        // When
        List<LicenseListResponse> result = licenseApplicationService.findByTrainerId(trainerId);

        // Then
        assertEquals(2, result.size());
        assertEquals("testA", result.get(0).getCertificateName());
        assertEquals("testB", result.get(1).getCertificateName());
    }

    @Test
    public void givenInvalidTrainerId_whenFindByTrainerId_thenReturnEmptyList() {
        // Given
        Long trainerId = 2L;
        given(licenseDomainService.findByTrainerId(trainerId)).willReturn(null);

        // When
        List<LicenseListResponse> result = licenseApplicationService.findByTrainerId(trainerId);

        // Then
        assertNull(result);
    }

    @Test
    public void givenSaveLicenseRequest_whenGetLicenseVerificationResult_thenLicenseResultReturned() {
        // Given
        SaveLicenseRequest request = new SaveLicenseRequest();
        request.setName("Test License");
        request.setNo("12345");

        ReflectionTestUtils.setField(licenseApplicationService, "licenseApiKey", licenseApiKey);
        ReflectionTestUtils.setField(licenseApplicationService, "licenseApiUri", licenseApiUri);

        // When
        LicenseResultResponse result = licenseApplicationService.getLicenseVerificationResult(request);

        // Then
        assertNotNull(result);
    }

    @Test
    public void givenValidLicenseResultResponseAndTrainerId_whenSave_thenLicenseIsSaved() throws Exception {
        // Given
        Long trainerId = 123L;
        LicenseResultResponse licenseResult = new LicenseResultResponse(/* 라이선스 결과 정보 생성 */);
        License savedLicense = new License
                (
                        123L,
                        "test",
                        "test",
                        trainerId
                );

        given(licenseDomainService.save(any(LicenseResultResponse.class), any(Long.class))).willReturn(savedLicense);

        // When
        License result = licenseApplicationService.save(licenseResult, trainerId);

        // Then
        assertEquals(savedLicense, result, "저장된 라이선스와 반환된 라이선스는 같아야 합니다.");
    }
}
