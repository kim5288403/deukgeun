package com.example.deukgeun.trainer.profile;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.ProfileRepository;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.request.ProfileRequest;
import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class GetProfileTest {
    @Autowired
    private ProfileServiceImpl profileService;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private String email;
    private String password;
    private Long profileId;
    private Long userId;
    @BeforeEach
    void setUp() {
        email = "testEmail@test.com";
        password = "testPassword1!2@";
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

        userId = saveUser.getId();
        Profile saveProfileData = ProfileRequest.create("fileName", userId);
        Profile saveProfile = profileRepository.save(saveProfileData);
        profileId = saveProfile.getId();
    }

    @Test
    void shouldGetProfileForValidProfileId() {
        // When
        Profile result = profileService.getProfile(profileId);

        // Then
        assertEquals(profileId, result.getId());
        assertEquals(userId, result.getUserId());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionForInvalidProfileId() {
        // Given
        Long invalidProfileId = 9999L;

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> {
            profileService.getProfile(invalidProfileId);
        });
    }
}
