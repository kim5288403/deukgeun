package com.example.deukgeun.trainer;

import com.example.deukgeun.commom.entity.AuthMail;
import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.commom.enums.MailStatus;
import com.example.deukgeun.commom.repository.AuthMailRepository;
import com.example.deukgeun.commom.request.AuthMailRequest;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;



import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserSaveTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthMailRepository authMailRepository;

    @BeforeEach
    void setUp() {
        System.out.println("-------------------- UserSaveTest Start --------------------");

        AuthMail authMail = AuthMailRequest.create("testEmail@test.com", "1234", MailStatus.Y);
        authMailRepository.save(authMail);
    }

    @Test
    void givenValidCredentials_whenSave_thenSucceed() {
        // Given
        UserRepository userRepository = mock(UserRepository.class);
        ProfileRepository profileRepository = mock(ProfileRepository.class);
        JwtServiceImpl jwtService = mock(JwtServiceImpl.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setName("테스트");
        joinRequest.setEmail("saveTest@test.com");
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

        UserServiceImpl userService = new UserServiceImpl(userRepository, profileRepository, jwtService, passwordEncoder);

        given(userRepository.save(any(User.class))).willReturn(user);

        // When
        User savedUser = userService.save(joinRequest);

        // Then
        assertThat(user).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void givenValidCredentials_whenSaveApi_thenSucceed() throws Exception {
        // Given
        Resource testFile = new ClassPathResource("/static/images/trainer/profile/testImage.png");
        MockMultipartFile profile = new MockMultipartFile("profile", "testImage.png", "image/png", testFile.getInputStream());

        // When
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/trainer/")
                        .file(profile)
                        .contentType("")
                        .param("processData", "false")
                        .param("name", "테스트이름")
                        .param("email", "testEmail@test.com")
                        .param("password", "testPassword1!2@")
                        .param("passwordConfirm", "testPassword1!2@")
                        .param("postcode", "testPostCode")
                        .param("jibunAddress", "testJibunAddress")
                        .param("roadAddress", "testRoadAddress")
                        .param("detailAddress", "testDetailAddress")
                        .param("extraAddress", "testExtraAddress")
                        .param("price", "30000")
                        .param("gender", "M")
                        .param("groupStatus", "Y")
                        .param("groupName", "testGroupName")
                        .param("code", "1234")
                        .param("introduction", "testIntroduction")
                )

                // then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("회원 가입 성공 했습니다."));
    }

    @AfterEach
    void reset() {
    }

}
