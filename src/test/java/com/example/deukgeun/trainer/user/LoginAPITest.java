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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LoginAPITest {
    @Autowired
    private MockMvc mockMvc;
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
    void shouldLoginApiForValidRequest() throws Exception {

        // When
        mockMvc.perform(post("/api/trainer/login")
                                .param("email", email)
                                .param("password", password)
                        )
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("로그인 성공 했습니다."))
                .andExpect(jsonPath("$.data.role").value("trainer"));
    }

    @Test
    void shouldLoginApiForInvalidRequest() throws Exception {

        // When
        mockMvc.perform(post("/api/trainer/login")
                        .param("email", "invalidEmail")
                        .param("password", password)
                )
                // Then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("request error!"))
                .andExpect(jsonPath("$.data.valid_loginRequest").value("이메일 형식이 아닙니다."));
    }
}
