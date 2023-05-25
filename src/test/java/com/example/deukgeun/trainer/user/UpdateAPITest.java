package com.example.deukgeun.trainer.user;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.request.JoinRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    void shouldUpdateAPIForValidRequest() throws Exception {
        // When
        mockMvc.perform(put("/api/trainer/")
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
//                        .param("groupStatus", "N")
                        .param("groupName", "")
                        .param("code", "1234")
                        .param("introduction", "testIntroduction"))
                // Then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("request error!"));
    }

}
