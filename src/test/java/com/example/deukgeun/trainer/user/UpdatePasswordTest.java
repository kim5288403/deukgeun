package com.example.deukgeun.trainer.user;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UpdatePasswordTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private String email;
    private String password;

    @BeforeEach
    void setUp() {
        email = "updatePassword@teat.com";
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
        userRepository.save(user);
    }

    @Test
    void shouldUpdatePasswordValidRequest() {
        // Given
        String newPassword = "newPassword1";
        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setNewPassword(newPassword);

        // When
        userService.updatePassword(request);
        User user = userRepository.findByEmail(email).orElse(null);

        // Then
        assertNotNull(user);
        assertEquals(email, user.getEmail());
        assertTrue(passwordEncoder.matches(newPassword, user.getPassword()));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionInvalidRequest() {
        // Given
        String newPassword = "newPassword1";
        String email = "invalidEmail";
        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setEmail(email);
        request.setPassword(password);

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updatePassword(request);
        });
    }

    @AfterEach
    void reset() {
        userRepository.deleteByEmail(email);
    }
}
