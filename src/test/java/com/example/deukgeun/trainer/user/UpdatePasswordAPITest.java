package com.example.deukgeun.trainer.user;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.JoinRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UpdatePasswordAPITest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
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
    void shouldUpdatePasswordAPIForValidRequest() throws Exception {
        // When
        mockMvc.perform(put("/api/trainer/password")
                        .param("email", email)
                        .param("password", password)
                        .param("newPassword", "PasswordUpdate1!")
                        .param("newPasswordConfirm", "PasswordUpdate1!"))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("비밀번호 변경 성공했습니다."));
    }

    @Test
    void shouldBadRequestAPIForInvalidRequest() throws Exception {
        // When
        mockMvc.perform(put("/api/trainer/password")
                        .param("email", "invalidEmail")
                        .param("password", "invalidPassword")
                        .param("newPassword", "PasswordUpdate1!")
                        .param("newPasswordConfirm", "PasswordUpdate1!"))
                // Then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("request error!"));
    }

}
