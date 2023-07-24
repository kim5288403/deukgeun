package com.example.deukgeun.trainer.repository;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
import com.example.deukgeun.trainer.infrastructure.persistence.entity.LicenseEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.entity.TrainerEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.repository.LicenseRepositoryImpl;
import com.example.deukgeun.trainer.infrastructure.persistence.repository.TrainerRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LicenseRepositoryTest {
    @Autowired
    private LicenseRepositoryImpl licenseRepositoryImpl;
    @Autowired
    private TrainerRepositoryImpl trainerRepositoryImpl;

    private Long trainerId;

    @BeforeEach
    void setUp() {
        TrainerEntity trainer = TrainerEntity
                .builder()
                .id(123L)
                .name("테스트")
                .email("testEmail@test.com")
                .password("test1234!")
                .gender(Gender.F)
                .groupStatus(GroupStatus.Y)
                .groupName("test")
                .introduction("test")
                .detailAddress("test")
                .extraAddress("test")
                .jibunAddress("test")
                .roadAddress("test")
                .postcode("test")
                .price(3000)
                .build();

        TrainerEntity saveTrainer = trainerRepositoryImpl.save(trainer);
        trainerId = saveTrainer.getId();
    }

    @Test
    void shouldNotNullRepository() {
        assertNotNull(licenseRepositoryImpl);
    }
    @Test
    void givenLicense_whenDeleteById_thenIsDeleted() {
        LicenseEntity license = LicenseEntity.builder()
                .trainerId(trainerId)
                .certificateName("testCertificateName")
                .licenseNumber("testLicenseNumber")
                .build();
        LicenseEntity saveLicense = licenseRepositoryImpl.save(license);

        // When
        licenseRepositoryImpl.deleteById(saveLicense.getId());

        // Then
        LicenseEntity deletedLicense = licenseRepositoryImpl.findById(saveLicense.getId()).orElse(null);
        assertNull(deletedLicense);
    }

    @Test
    void givenLicenses_whenFindByTrainerId_thenReturnValid() {
        // Given
        LicenseEntity license1 = LicenseEntity.builder()
                .trainerId(trainerId)
                .certificateName("testCertificateName1")
                .licenseNumber("testLicenseNumber1")
                .build();

        LicenseEntity license2 = LicenseEntity.builder()
                .trainerId(trainerId)
                .certificateName("testCertificateName2")
                .licenseNumber("testLicenseNumber2")
                .build();

        licenseRepositoryImpl.save(license1);
        licenseRepositoryImpl.save(license2);

        // When
        List<LicenseEntity>  licenseList = licenseRepositoryImpl.findByTrainerId(trainerId);

        // Then
        assertEquals(2, licenseList.size());

        LicenseEntity licenseResponse1 = licenseList.get(0);
        assertEquals(license1.getLicenseNumber(), licenseResponse1.getLicenseNumber());

        LicenseEntity licenseResponse2 = licenseList.get(1);
        assertEquals(license2.getLicenseNumber(), licenseResponse2.getLicenseNumber());
    }

    @Test
    void givenLicense_whenSave_thenReturnValid() {
        // Given
        LicenseEntity license = LicenseEntity.builder()
                .trainerId(trainerId)
                .certificateName("testCertificateName")
                .licenseNumber("testLicenseNumber")
                .build();

        // When
        LicenseEntity saveLicense = licenseRepositoryImpl.save(license);

        // Then
        LicenseEntity foundLicense = licenseRepositoryImpl.findById(saveLicense.getId()).orElse(null);
        assertNotNull(foundLicense);
        assertEquals(saveLicense.getCertificateName(), foundLicense.getCertificateName());
        assertEquals(saveLicense.getLicenseNumber(), foundLicense.getLicenseNumber());
    }
}
