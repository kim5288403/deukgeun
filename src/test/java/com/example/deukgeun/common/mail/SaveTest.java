package com.example.deukgeun.common.mail;

import com.example.deukgeun.commom.entity.AuthMail;
import com.example.deukgeun.commom.enums.MailStatus;
import com.example.deukgeun.commom.repository.AuthMailRepository;
import com.example.deukgeun.commom.request.AuthMailRequest;
import com.example.deukgeun.commom.service.implement.MailServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class SaveTest {
    @Autowired
    private MailServiceImpl mailService;
    @Autowired
    private AuthMailRepository authMailRepository;

    @Test
    void shouldSaveForValidParameter() {
        // Given
        String email = "testEmail@Email.com";
        boolean before = authMailRepository.existsByEmail(email);
        String authCode = createCodeTest();

        // When
        mailService.save(email, authCode);
        AuthMail result = authMailRepository.findByEmail(email).orElse(null);

        // Then
        assertNotNull(result);
        assertFalse(before);
        assertEquals(email, result.getEmail());
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
