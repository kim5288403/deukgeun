package com.example.deukgeun.trainer.user;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SaveTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl mockUserService;
    @Autowired
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        System.out.println("-------------------- UserSaveTest Start --------------------");
    }
    @Test
    void shouldSaveUserValidJoinRequest() {
        // Given
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setName("테스트");
        joinRequest.setEmail(anyString());
        joinRequest.setPassword("test1!2@");
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

        given(userRepository.save(any(User.class))).willReturn(user);

        // When
        User savedUser = mockUserService.save(joinRequest);

        // Then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowDataIntegrityViolationExceptionInValidJoinRequest() {
        // Given
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setName("");
        joinRequest.setEmail(anyString());
        joinRequest.setPassword("test1!2@");
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

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> {
            userService.save(joinRequest);
        });
    }

    @Test
    public void shouldCreateUserFromValidJoinRequest() {
        // Given
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setName("John Doe");
        joinRequest.setEmail("johndoe@example.com");
        joinRequest.setPassword("password123");
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        // When
        User user = JoinRequest.create(joinRequest, passwordEncoder);

        // Then
        assertEquals(joinRequest.getName(), user.getName());
        assertEquals(joinRequest.getEmail(), user.getEmail());
        assertEquals(passwordEncoder.encode(joinRequest.getPassword()), user.getPassword());
        assertNotNull(user);
    }
}
