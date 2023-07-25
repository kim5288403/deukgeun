package com.example.deukgeun.trainer.service;

import com.example.deukgeun.trainer.application.dto.response.LicenseListResponse;
import com.example.deukgeun.trainer.application.dto.response.LicenseResultResponse;
import com.example.deukgeun.trainer.domain.model.entity.License;
import com.example.deukgeun.trainer.domain.repository.LicenseRepository;
import com.example.deukgeun.trainer.domain.service.implement.LicenseDomainServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LicenseDomainServiceTest {
    @InjectMocks
    private LicenseDomainServiceImpl licenseDomainService;
    @Mock
    private LicenseRepository licenseRepository;

    @Test
    public void givenLicenseId_whenDeleteById_thenRepositoryCalled() {
        // Given
        Long licenseId = 1L;

        // When
        licenseDomainService.deleteById(licenseId);

        // Then
        verify(licenseRepository, times(1)).deleteById(licenseId);
    }

    @Test
    public void givenValidTrainerId_whenFindByTrainerId_thenReturnLicenseListResponse() {
        // Given
        Long trainerId = 1L;
        LicenseListResponse licenseListResponseA = new LicenseListResponse
                (
                1L,
                        "test",
                        "test",
                        LocalDateTime.now()
                );
        LicenseListResponse licenseListResponseB = new LicenseListResponse
                (
                1L,
                "test",
                        "test",
                        LocalDateTime.now()
                );

        List<LicenseListResponse> licenses = Arrays.asList(licenseListResponseA, licenseListResponseB);

        given(licenseRepository.findByTrainerId(trainerId)).willReturn(licenses);

        // When
        List<LicenseListResponse> result = licenseDomainService.findByTrainerId(trainerId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(licenses.get(0).getLicenseId(), result.get(0).getLicenseId());
        assertEquals(licenses.get(0).getCertificateName(), result.get(0).getCertificateName());
        assertEquals(licenses.get(1).getLicenseId(), result.get(1).getLicenseId());
        assertEquals(licenses.get(1).getCertificateName(), result.get(1).getCertificateName());
    }

    @Test
    public void givenInvalidTrainerId_whenFindByTrainerId_thenReturnEmptyList() {
        // Given
        Long trainerId = 2L;
        given(licenseRepository.findByTrainerId(trainerId)).willReturn(null);

        // When
        List<LicenseListResponse> result = licenseDomainService.findByTrainerId(trainerId);

        // Then
        assertNull(result);
    }

    @Test
    public void givenLicenseResultAndTrainerId_whenSave_thenLicenseSavedAndReturned() {
        // Given
        Long trainerId = 1L;

        LicenseResultResponse licenseResult = new LicenseResultResponse();
        licenseResult.setCertificatename("License A");
        licenseResult.setNo("1234567890");

        License expectedLicense = new License
                (
                        123L,
                        licenseResult.getCertificatename(),
                        licenseResult.getNo(),
                        trainerId
                );

        given(licenseRepository.save(expectedLicense)).willReturn(expectedLicense);

        // When
        License result = licenseDomainService.save(licenseResult, trainerId);

        // Then
        assertNotNull(result);
        assertEquals(expectedLicense.getCertificateName(), result.getCertificateName());
        assertEquals(expectedLicense.getTrainerId(), result.getTrainerId());
        assertEquals(expectedLicense.getLicenseNumber(), result.getLicenseNumber());
        verify(licenseRepository, times(1)).save(any(License.class));
    }
}
