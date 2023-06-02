package com.example.deukgeun.common.mail;

import com.example.deukgeun.commom.entity.AuthMail;
import com.example.deukgeun.commom.enums.MailStatus;
import com.example.deukgeun.commom.repository.AuthMailRepository;
import com.example.deukgeun.commom.request.AuthMailRequest;
import com.example.deukgeun.commom.service.implement.MailServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ConfirmTest {
    @Autowired
    private AuthMailRepository authMailRepository;
    @Autowired
    private MailServiceImpl mailService;
    private Long id;

    @Test
    void shouldConfirmForValidRequest() {
        // Given
        String email = "testEmail@Email.com";
        String authCode = createCodeTest();
        AuthMail authMail = AuthMailRequest.create(email, authCode, MailStatus.N);
        AuthMail saveAuthMail = authMailRepository.save(authMail);
        id = saveAuthMail.getId();
        AuthMailRequest request = new AuthMailRequest();
        request.setEmail(email);
        request.setCode(authCode);

        // When
        mailService.confirm(request);
        AuthMail result = authMailRepository.findById(id).orElse(null);

        // Then
        assertNotNull(result);
        assertEquals(saveAuthMail.getEmail(), result.getEmail());
        assertNotEquals(saveAuthMail.getStatus(), result.getStatus());
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
