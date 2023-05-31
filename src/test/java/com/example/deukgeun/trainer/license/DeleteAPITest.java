package com.example.deukgeun.trainer.license;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.License;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.LicenseRepository;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.request.SaveLicenseRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DeleteAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LicenseRepository licenseRepository;

    @Test
    void shouldDeleteAPIForValidRequest() throws Exception {
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

        License license1 = SaveLicenseRequest.create("테스트 자격증1", "1t2e3s4t", saveUser.getId());
        License license2 = SaveLicenseRequest.create("테스트 자격증2", "1t2e3s4t", saveUser.getId());
        License saveLicense1 = licenseRepository.save(license1);
        License saveLicense2 = licenseRepository.save(license2);
        String ids = saveLicense1.getId() + "," + saveLicense2.getId();

        // When
        mockMvc.perform(delete("/api/trainer/license/")
                        .param("ids", ids)
                )
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("자격증 삭제 성공했습니다."));
    }

    @Test
    void shouldBadRequestForInvalidRequest() throws Exception {
        // Given
        String ids = "99999, 999999";

        // When
        mockMvc.perform(delete("/api/trainer/license/")
                        .param("ids", ids)
                )
                // Then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No class com.example.deukgeun.trainer.entity.License entity with id 99999 exists!"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmptyResultDataAccessException));
    }
}
