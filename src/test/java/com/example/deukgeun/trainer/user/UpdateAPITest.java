package com.example.deukgeun.trainer.user;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.JoinRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
public class UpdateAPITest {
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
        userRepository.save(user);
    }

    @Test
    void shouldUpdateAPIForValidRequest() throws Exception {
        // When
        mockMvc.perform(put("/api/trainer/")
                        .param("name", "테스트이름")
                        .param("email", email)
                        .param("password", password)
                        .param("postcode", "testPostCode")
                        .param("jibunAddress", "testJibunAddress")
                        .param("roadAddress", "testRoadAddress")
                        .param("detailAddress", "testDetailAddress")
                        .param("extraAddress", "testExtraAddress")
                        .param("price", "30000")
                        .param("gender", "M")
                        .param("groupStatus", "N")
                        .param("groupName", "")
                        .param("code", "1234")
                        .param("introduction", "testIntroduction"))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("내 정보 수정 성공했습니다."));
    }

    @Test
    void shouldBadRequestForInvalidRequest() throws Exception {
        // When
        mockMvc.perform(put("/api/trainer/")
                        .param("name", "테스트이름")
                        .param("email", email)
                        .param("password", password)
                        .param("postcode", "testPostCode")
                        .param("jibunAddress", "testJibunAddress")
                        .param("roadAddress", "testRoadAddress")
                        .param("detailAddress", "testDetailAddress")
                        .param("extraAddress", "testExtraAddress")
                        .param("price", "30000")
                        .param("gender", "M")
//                        .param("groupStatus", "N")
                        .param("groupName", "")
                        .param("code", "1234")
                        .param("introduction", "testIntroduction"))
                // Then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("request error!"));
    }

}
