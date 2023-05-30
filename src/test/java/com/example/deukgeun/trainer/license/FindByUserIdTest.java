package com.example.deukgeun.trainer.license;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.License;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.LicenseRepository;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.response.LicenseListResponse;
import com.example.deukgeun.trainer.service.implement.LicenseServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class FindByUserIdTest {

    @Autowired
    private LicenseServiceImpl licenseService;
    @Autowired
    private LicenseRepository licenseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldFindByUserIdForValidParameter() {
        // Given
        String email = "testEmail@test.com";
        String password = "testPassword1!2@";
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setEmail(email);
        joinRequest.setName("테스트");
        joinRequest.setPassword(password);
        joinRequest.setGroupStatus(GroupStatus.Y);
        joinRequest.setGroupName("testGroupName");
        joinRequest.setPostcode("testPostCode");
        joinRequest.setJibunAddress("testJibunAddress");
        joinRequest.setRoadAddress("testRoadAddress");
        joinRequest.setDetailAddress("testDetailAddress");
        joinRequest.setExtraAddress("testExtraAddress");
        joinRequest.setGender(Gender.M);
        joinRequest.setPrice(30000);
        joinRequest.setIntroduction("testIntroduction");
        User user = JoinRequest.create(joinRequest, passwordEncoder);
        User saveUser = userRepository.save(user);
        Long saveUserId = saveUser.getId();

        String certificateName = "테스트자격증";
        String licenseNumber = "t1e2s3t4";
        License license = SaveLicenseRequest.create(certificateName, licenseNumber, saveUserId);
        License saveLicense = licenseRepository.save(license);

        // When
        List<LicenseListResponse> result = licenseService.findByUserId(saveUserId);

        // Given
        assertEquals(1, result.size());
        assertEquals(certificateName, result.get(0).getCertificateName());
        assertEquals(licenseNumber, result.get(0).getLicenseNumber());
        assertEquals(saveLicense.getId(), result.get(0).getLicenseId());
    }
    @Test
    void shouldEmptyDataForInvalidParameter() {
        // Given
        long userId = 9999;

        // When
        List<LicenseListResponse> result = licenseService.findByUserId(userId);

        // Given
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }
}
