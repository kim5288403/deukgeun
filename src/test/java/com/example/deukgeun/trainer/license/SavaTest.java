package com.example.deukgeun.trainer.license;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.License;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.response.LicenseResultResponse;
import com.example.deukgeun.trainer.service.implement.LicenseServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class SavaTest {
    @Autowired
    private LicenseServiceImpl licenseService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldSaveForValidParameter() throws Exception {
        // Given
        String email = "loginTest@email.com";
        String password = "test1!2@";

        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setName("테스트");
        joinRequest.setEmail(email);
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

        LicenseResultResponse response = new LicenseResultResponse();
        response.setResult(true);
        response.setNo("t1e2s3t4");
        response.setCertificatename("테스트 자격증");

        // When
        License result = licenseService.save(response, saveUser.getId());

        // Then
        assertNotNull(result);
        assertEquals(response.getCertificatename(), result.getCertificateName());
        assertEquals(saveUser.getId(), result.getUserId());
        assertEquals(response.getNo(), result.getLicenseNumber());
    }

    @Test
    void shouldDataIntegrityViolationExceptionForInvalidUserId() {
        // Given
        LicenseResultResponse response = new LicenseResultResponse();
        response.setResult(true);
        response.setNo("t1e2s3t4");
        response.setCertificatename("테스트 자격증");

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> {
            licenseService.save(response, 9999L);
        });
    }

    @Test
    void shouldDataIntegrityViolationExceptionForInvalidLicenseResultResponse() {
        // Given
        String email = "loginTest@email.com";
        String password = "test1!2@";

        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setName("테스트");
        joinRequest.setEmail(email);
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

        LicenseResultResponse response = new LicenseResultResponse();

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> {
            licenseService.save(response, saveUser.getId());
        });
    }
}
