package com.example.deukgeun.trainer.license;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.License;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.LicenseRepository;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.service.implement.LicenseServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class DeleteTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LicenseRepository licenseRepository;
    @Autowired
    private LicenseServiceImpl licenseService;

    @Test
    void shouldDeleteForValidParameter() {
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

        License license = SaveLicenseRequest.create("테스트 자격증1", "1t2e3s4t", saveUser.getId());
        License saveLicense = licenseRepository.save(license);

        // When
        licenseService.delete(saveLicense.getId());
        License result = licenseRepository.findById(saveLicense.getId()).orElse(null);

        // Then
        assertNull(result);
    }

    @Test
    void shouldEmptyResultDataAccessExceptionForInvalidParameter() {
        // Given
        long id = 99999;

        // When, Then
        assertThrows(EmptyResultDataAccessException.class, () -> {
            licenseService.delete(id);
        });
    }

}
