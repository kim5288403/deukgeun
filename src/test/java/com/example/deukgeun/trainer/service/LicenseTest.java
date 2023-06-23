package com.example.deukgeun.trainer.service;

import com.example.deukgeun.trainer.entity.License;
import com.example.deukgeun.trainer.repository.LicenseRepository;
import com.example.deukgeun.trainer.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.response.LicenseListResponse;
import com.example.deukgeun.trainer.response.LicenseResultResponse;
import com.example.deukgeun.trainer.service.implement.LicenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class LicenseTest {
    @InjectMocks
    private LicenseServiceImpl licenseService;
    @Mock
    private LicenseRepository licenseRepository;
    @Value("${trainer.license.api.key}")
    private String licenseApiKey;
    @Value("${trainer.license.api.uri}")
    private String licenseApiUri;


    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(licenseService, "licenseApiKey", licenseApiKey);
        ReflectionTestUtils.setField(licenseService, "licenseApiUri", licenseApiUri);

    }

    @Test
    public void givenValidMemberId_whenFindByMemberId_thenReturnLicenseListResponse() {
        // Given
        Long memberId = 1L;
        License LicenseA = License
                .builder()
                .memberId(1L)
                .certificateName("LicenseA")
                .licenseNumber("t1e2s3t4")
                .build();
        License LicenseB = License
                .builder()
                .memberId(2L)
                .certificateName("LicenseB")
                .licenseNumber("t1e2s3t4")
                .build();


        List<LicenseListResponse> licenses = Arrays.asList(new LicenseListResponse(LicenseA), new LicenseListResponse(LicenseB));

        given(licenseRepository.findByMemberId(memberId)).willReturn(licenses);

        // When
        List<LicenseListResponse> result = licenseService.findByMemberId(memberId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(licenses.get(0).getLicenseId(), result.get(0).getLicenseId());
        assertEquals(licenses.get(0).getCertificateName(), result.get(0).getCertificateName());
        assertEquals(licenses.get(1).getLicenseId(), result.get(1).getLicenseId());
        assertEquals(licenses.get(1).getCertificateName(), result.get(1).getCertificateName());
    }

    @Test
    public void givenInvalidMemberId_whenFindByMemberId_thenReturnEmptyList() {
        // Given
        Long memberId = 2L;
        given(licenseRepository.findByMemberId(memberId)).willReturn(null);

        // When
        List<LicenseListResponse> result = licenseService.findByMemberId(memberId);

        // Then
        assertNull(result);
    }

    @Test
    public void givenLicenseResultAndMemberId_whenSave_thenLicenseSavedAndReturned() throws Exception {
        // Given
        Long memberId = 1L;

        LicenseResultResponse licenseResult = new LicenseResultResponse();
        licenseResult.setCertificatename("License A");
        licenseResult.setNo("1234567890");

        License expectedLicense = License.builder()
                .certificateName(licenseResult.getCertificatename())
                .memberId(memberId)
                .licenseNumber(licenseResult.getNo())
                .build();

        given(licenseRepository.save(expectedLicense)).willReturn(expectedLicense);

        // When
        License result = licenseService.save(licenseResult, memberId);

        // Then
        assertNotNull(result);
        assertEquals(expectedLicense.getCertificateName(), result.getCertificateName());
        assertEquals(expectedLicense.getMemberId(), result.getMemberId());
        assertEquals(expectedLicense.getLicenseNumber(), result.getLicenseNumber());
        verify(licenseRepository, times(1)).save(any(License.class));
    }

    @Test
    public void givenLicenseId_whenDelete_thenRepositoryCalled() {
        // Given
        Long licenseId = 1L;

        // When
        licenseService.delete(licenseId);

        // Then
        verify(licenseRepository, times(1)).deleteById(licenseId);
    }

    @Test
    public void givenSaveLicenseRequest_whenGetLicenseVerificationResult_thenLicenseResultReturned() {
        // Given
        SaveLicenseRequest request = new SaveLicenseRequest();
        request.setName("Test License");
        request.setNo("12345");

        // When
        LicenseResultResponse result = licenseService.getLicenseVerificationResult(request);

        // Then
        assertNotNull(result);
    }

    @Test
    public void givenValidLicenseResultResponse_whenCheckingLicense_thenNoExceptionThrown() {
        // Given
        LicenseResultResponse result = new LicenseResultResponse();
        result.setResult(true);
        result.setCertificatename("test");
        result.setNo("12345");

        // When & Then
        assertDoesNotThrow(() -> licenseService.checkLicense(result));
    }

    @Test
    public void givenInvalidLicenseResultResponse_whenCheckingLicense_thenIllegalArgumentExceptionThrown() {
        // Given
        LicenseResultResponse result = new LicenseResultResponse();
        result.setResult(false);
        result.setCertificatename("test");
        result.setNo("12345");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> licenseService.checkLicense(result));
    }

}
