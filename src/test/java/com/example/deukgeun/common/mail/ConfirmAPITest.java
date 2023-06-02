package com.example.deukgeun.common.mail;

import com.example.deukgeun.commom.entity.AuthMail;
import com.example.deukgeun.commom.enums.MailStatus;
import com.example.deukgeun.commom.repository.AuthMailRepository;
import com.example.deukgeun.commom.request.AuthMailRequest;
import com.example.deukgeun.commom.service.implement.MailServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ConfirmAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthMailRepository authMailRepository;

    private Long id;

    @Test
    void shouldConfirmAPIForValidRequest() throws Exception {
        // Given
        String email = "testEmail@Email.com";
        String authCode = createCodeTest();
        AuthMail authMail = AuthMailRequest.create(email, authCode, MailStatus.N);
        AuthMail saveAuthMail = authMailRepository.save(authMail);
        id = saveAuthMail.getId();

        // When
        mockMvc.perform(post("/api/mail/confirm")
                        .param("email", email)
                        .param("code", authCode)
                )
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("메일 인증 성공 했습니다."));

    }

    @AfterEach
    void reset() {
        authMailRepository.deleteById(id);
    }
    private String createCodeTest() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    // 소문자 알파벳
                    key.append((char) ((int) random.nextInt(26) + 97));
                    break;
                case 1:
                    // 대문자 알파벳
                    key.append((char) ((int) random.nextInt(26) + 65));
                    break;
                case 2:
                    // 숫자
                    key.append(random.nextInt(9));
                    break;
            }
        }
        return key.toString();
    }

}
