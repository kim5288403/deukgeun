package com.example.deukgeun.trainer.user;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.commom.exception.PasswordMismatchException;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.request.LoginRequest;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
public class LoginTest {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private String email;
    private String password;

    @BeforeEach
    void setup() {
        email = "loginTest@email.com";
        password = "test1!2@";

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
        userRepository.save(user);
    }

    @Test
    void shouldLoginForValidRequest() {
        // Given
        LoginRequest request = new LoginRequest(email, password);

        // When, Then
        assertDoesNotThrow( () -> {
            userService.login(request);
        });
    }

    @Test
    void shouldEntityNotFoundExceptionForInvalidRequest() {
        // Given
        LoginRequest request = new LoginRequest("invalidEmail@email.com", password);

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> {
            userService.login(request);
        });
    }

    @Test
    void shouldPasswordMismatchExceptionForInvalidRequest() {
        // Given
        LoginRequest request = new LoginRequest(email, "invalidPassword");

        // When, Then
        assertThrows(PasswordMismatchException.class, () -> {
            userService.login(request);
        });
    }
}
