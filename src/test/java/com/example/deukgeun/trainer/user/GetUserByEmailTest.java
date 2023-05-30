package com.example.deukgeun.trainer.user;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import groovy.transform.AutoImplement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GetUserByEmailTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserServiceImpl userService;

    @Test
    void shouldGetUserByEmailForValidParameter() {
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
        userRepository.save(user);

        // When
        User result = userService.getUserByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void shouldEntityNotFoundExceptionIdForInvalidParameter() {
        // Given
        String email = "testEmail@test.com";

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserByEmail(email);
        });
    }

}
