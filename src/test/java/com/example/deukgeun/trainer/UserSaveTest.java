package com.example.deukgeun.trainer;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.ProfileRepository;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class UserSaveTest {

    @BeforeEach
    void setUp() {
        System.out.println("-------------------- UserSaveTest Start --------------------");
    }
    @Test
    void givenValidCredentials_whenSave_thenSucceed() {
        // Given
        UserRepository userRepository = mock(UserRepository.class);
        ProfileRepository profileRepository = mock(ProfileRepository.class);
        JwtServiceImpl jwtService = mock(JwtServiceImpl.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        User user = JoinRequest.create(
                "테스트",
                "saveTest@test.com",
                passwordEncoder.encode("test1!2@"),
                GroupStatus.Y,
                "testGroupName",
                "testPostCode",
                "testJibunAddress",
                "testRoadAddress",
                "testDetailAddress",
                "testExtraAddress",
                Gender.M,
                30000,
                "testIntroduction"
        );
        given(userRepository.save(user)).willReturn(user);

        UserServiceImpl userService = new UserServiceImpl(userRepository, profileRepository, jwtService);

        // When
        User savedUser = userService.save(user);

        // Then
        assertThat(user).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void givenInvalidCredentials_whenSave_thenFailed() {
        // Given
        UserRepository userRepository = mock(UserRepository.class);
        ProfileRepository profileRepository = mock(ProfileRepository.class);
        JwtServiceImpl jwtService = mock(JwtServiceImpl.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        User user = new User();

        given(userRepository.save(user)).willReturn(user);

        UserServiceImpl userService = new UserServiceImpl(userRepository, profileRepository, jwtService);

        // When
        User savedUser = userService.save(user);

        // Then
        assertThat(user).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
        verify(userRepository).save(user);
    }

    @AfterEach
    void reset() {
    }

}
