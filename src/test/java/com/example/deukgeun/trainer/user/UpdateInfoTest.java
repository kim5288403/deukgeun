package com.example.deukgeun.trainer.user;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UpdateInfoTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private String email;

    @BeforeEach
    void setUp() {
        email = "testUpdate@teat.com";
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setEmail(email);
        joinRequest.setName("테스트");
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
    void shouldUpdateInfoValidUpdateInfoRequest() throws Exception {
        // Given
        UpdateInfoRequest request = new UpdateInfoRequest();
        request.setName("테스트업데이트");
        request.setEmail(email);
        request.setGroupStatus(GroupStatus.Y);
        request.setGroupName("testGroupName");
        request.setPostcode("testPostCode");
        request.setJibunAddress("testJibunAddress");
        request.setRoadAddress("testRoadAddress");
        request.setDetailAddress("testDetailAddress");
        request.setExtraAddress("testExtraAddress");
        request.setGender(Gender.M);
        request.setPrice(30000);
        request.setIntroduction("testIntroduction");

        // When
        userService.updateInfo(request);
        User result = userRepository.findByEmail(email).orElse(null);

        // Then
        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
    }

    @Test
    void shouldReturnNullInvalidUpdateInfoRequest() throws Exception {
        // Given
        String email = "invalidEmail@test.com";
        UpdateInfoRequest request = new UpdateInfoRequest();
        request.setName(email);
        request.setEmail("invalidEmail@test.com");
        request.setGroupStatus(GroupStatus.Y);
        request.setGroupName("testGroupName");
        request.setPostcode("testPostCode");
        request.setJibunAddress("testJibunAddress");
        request.setRoadAddress("testRoadAddress");
        request.setDetailAddress("testDetailAddress");
        request.setExtraAddress("testExtraAddress");
        request.setGender(Gender.M);
        request.setPrice(30000);
        request.setIntroduction("testIntroduction");

        // When
        userService.updateInfo(request);
        User result = userRepository.findByEmail(email).orElse(null);

        // Then
        assertNull(result);
    }

    @AfterEach
    void reset() {
        userRepository.deleteByEmail(email);
    }

}
