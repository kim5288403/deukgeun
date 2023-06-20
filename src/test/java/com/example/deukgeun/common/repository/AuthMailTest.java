package com.example.deukgeun.common.repository;

import com.example.deukgeun.commom.entity.AuthMail;
import com.example.deukgeun.commom.enums.MailStatus;
import com.example.deukgeun.commom.repository.AuthMailRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthMailTest {
    @Autowired
    private AuthMailRepository authMailRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(authMailRepository);
    }

    @Test
    void givenAuthMail_whenSaved_thenReturnValid() {
        // Given
        String email = "testEmail@test.com";
        String code = "1t2e3s4t";
        MailStatus mailStatus = MailStatus.N;
        AuthMail authMail  = AuthMail
                .builder()
                .email(email)
                .code(code)
                .mailStatus(mailStatus)
                .build();

        // When
        AuthMail saveAuthMail = authMailRepository.save(authMail);

        // Then
        AuthMail  retrievedAuthMail = authMailRepository.findById(saveAuthMail.getId()).orElse(null);
        assertNotNull(retrievedAuthMail);
        assertEquals(saveAuthMail.getEmail(), retrievedAuthMail.getEmail());
        assertEquals(saveAuthMail.getCode(), retrievedAuthMail.getCode());
    }

    @Test
    void givenAuthMail_whenExistsByEmailAndCode_thenReturnsTrue() {
        // Given
        String email = "testEmail@test.com";
        String code = "1t2e3s4t";
        MailStatus mailStatus = MailStatus.N;
        AuthMail authMail  = AuthMail
                .builder()
                .email(email)
                .code(code)
                .mailStatus(mailStatus)
                .build();
        authMailRepository.save(authMail);

        // When
        boolean emailAndCodeExist = authMailRepository.existsByEmailAndCode(email, code);

        // Then
        assertTrue(emailAndCodeExist);
    }

    @Test
    void givenAuthMail_whenExistsByEmail_thenReturnsTrue() {
        // Given
        String email = "testEmail@test.com";
        String code = "1t2e3s4t";
        MailStatus mailStatus = MailStatus.N;
        AuthMail authMail  = AuthMail
                .builder()
                .email(email)
                .code(code)
                .mailStatus(mailStatus)
                .build();
        authMailRepository.save(authMail);

        // When
        boolean emailExists = authMailRepository.existsByEmail(email);

        // Then
        assertTrue(emailExists);
    }

    @Test
    void givenAuthMail_whenFindByEmail_thenReturnValid() {
        // Given
        String email = "testEmail@test.com";
        String code = "1t2e3s4t";
        MailStatus mailStatus = MailStatus.N;
        AuthMail authMail  = AuthMail
                .builder()
                .email(email)
                .code(code)
                .mailStatus(mailStatus)
                .build();
        authMailRepository.save(authMail);

        // When
        AuthMail findAuthMail = authMailRepository.findByEmail(authMail.getEmail()).orElse(null);

        // Then
        assertNotNull(findAuthMail);
        assertEquals(findAuthMail.getEmail(), authMail.getEmail());
        assertEquals(findAuthMail.getCode(), authMail.getCode());
    }

    @Test
    void givenAuthMail_whenDeleteByEmail_thenIsDeleted() {
        // Given
        String email = "testEmail@test.com";
        String code = "1t2e3s4t";
        MailStatus mailStatus = MailStatus.N;
        AuthMail authMail  = AuthMail
                .builder()
                .email(email)
                .code(code)
                .mailStatus(mailStatus)
                .build();
        authMailRepository.save(authMail);

        // When
        authMailRepository.deleteByEmail(email);

        // Then
        boolean emailExists = authMailRepository.existsByEmail(email);
        assertFalse(emailExists);
    }

    @Test
    void givenAuthMail_whenUpdateMailStatus_thenEmailIsDeleted() {
        // Given
        String email = "testEmail@test.com";
        String code = "1t2e3s4t";
        MailStatus mailStatus = MailStatus.N;
        AuthMail authMail  = AuthMail
                .builder()
                .email(email)
                .code(code)
                .mailStatus(mailStatus)
                .build();
        authMailRepository.save(authMail);
        AuthMail findAuthMail = authMailRepository.findByEmail(email).orElse(null);

        // When
        assert findAuthMail != null;
        findAuthMail.updateMailStatus(MailStatus.Y);
        authMailRepository.save(findAuthMail);

        // Then
        AuthMail resultAuthMail = authMailRepository.findById(findAuthMail.getId()).orElse(null);
        assertNotNull(resultAuthMail);
        assertEquals(findAuthMail.getId(), resultAuthMail.getId());
        assertNotEquals(mailStatus, resultAuthMail.getMailStatus());
    }
}
