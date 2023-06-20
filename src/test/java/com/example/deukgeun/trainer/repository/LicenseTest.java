package com.example.deukgeun.trainer.repository;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.License;
import com.example.deukgeun.trainer.entity.Member;
import com.example.deukgeun.trainer.response.LicenseListResponse;
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
public class LicenseTest {

    @Autowired
    private LicenseRepository licenseRepository;
    @Autowired
    private MemberRepository memberRepository;

    private long memberId;
    @Test
    void shouldNotNullRepository() {
        assertNotNull(licenseRepository);
    }

    @BeforeEach
    void setUp() {
        Member member = Member
                .builder()
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

        Member saveMember = memberRepository.save(member);
        memberId = saveMember.getId();
    }

    @Test
    void givenLicense_whenSaved_thenReturnValid() {
        // Given
        License license = License.builder()
                .memberId(memberId)
                .certificateName("testCertificateName")
                .licenseNumber("testLicenseNumber")
                .build();

        // When
        License saveLicense = licenseRepository.save(license);

        // Then
        License foundLicense = licenseRepository.findById(memberId).orElse(null);
        assertNotNull(foundLicense);
        assertEquals(saveLicense.getCertificateName(), foundLicense.getCertificateName());
        assertEquals(saveLicense.getLicenseNumber(), foundLicense.getLicenseNumber());
    }

    @Test
    void givenLicenses_whenFindByMemberId_thenReturnValid() {
        // Given
        License license1 = License.builder()
                .memberId(memberId)
                .certificateName("testCertificateName1")
                .licenseNumber("testLicenseNumber1")
                .build();

        License license2 = License.builder()
                .memberId(memberId)
                .certificateName("testCertificateName2")
                .licenseNumber("testLicenseNumber2")
                .build();

        licenseRepository.save(license1);
        licenseRepository.save(license2);

        // When
        List<LicenseListResponse> licenseList = licenseRepository.findByMemberId(memberId);

        // Then
        assertEquals(2, licenseList.size());

        LicenseListResponse licenseResponse1 = licenseList.get(0);
        assertEquals(license1.getLicenseNumber(), licenseResponse1.getLicenseNumber());

        LicenseListResponse licenseResponse2 = licenseList.get(1);
        assertEquals(license2.getLicenseNumber(), licenseResponse2.getLicenseNumber());
    }

    @Test
    void givenLicense_whenDeleteById_thenIsDeleted() {
        License license = License.builder()
                .memberId(memberId)
                .certificateName("testCertificateName")
                .licenseNumber("testLicenseNumber")
                .build();
        License saveLicense = licenseRepository.save(license);

        // When
        licenseRepository.deleteById(saveLicense.getId());

        // Then
        License deletedLicense = licenseRepository.findById(saveLicense.getId()).orElse(null);
        assertNull(deletedLicense);
    }

}
