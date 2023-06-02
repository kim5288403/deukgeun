package com.example.deukgeun.common.mail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SendAPITest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldSendAPIForValidRequest() throws Exception {
        // Given
        String email = "testEmail@email.com";

        // When
        mockMvc.perform(post("/api/mail/send")
                        .param("email", email)
                )
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("인증 메일 보내기 성공했습니다."));
    }
}
