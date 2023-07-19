package com.example.deukgeun.auth.repository;

import com.example.deukgeun.auth.domain.model.valueobject.MailStatus;
import com.example.deukgeun.auth.infrastructure.persistence.entity.AuthMailEntity;
import com.example.deukgeun.auth.infrastructure.persistence.repository.AuthMailRepositoryImpl;
import com.example.deukgeun.global.util.LongIdGeneratorUtil;
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
public class AuthMailRepositoryTest {
    @Autowired
    private AuthMailRepositoryImpl authMailRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(authMailRepository);
    }

    @Test
    void givenAuthMail_whenDeleteByEmail_thenIsDeleted() {
        // Given
        String email = "testEmail@test.com";
        String code = "1t2e3s4t";
        MailStatus mailStatus = MailStatus.N;
        AuthMailEntity authMail = AuthMailEntity
                .builder()
                .id(LongIdGeneratorUtil.gen())
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
    void givenAuthMail_whenExistsByEmail_thenReturnsTrue() {
        // Given
        String email = "testEmail@test.com";
        String code = "1t2e3s4t";
        MailStatus mailStatus = MailStatus.N;
        AuthMailEntity authMail = AuthMailEntity
                .builder()
                .id(LongIdGeneratorUtil.gen())
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
    void givenAuthMail_whenExistsByEmailAndCode_thenReturnsTrue() {
        // Given
        String email = "testEmail@test.com";
        String code = "1t2e3s4t";
        MailStatus mailStatus = MailStatus.N;
        AuthMailEntity authMail = AuthMailEntity
                .builder()
                .id(LongIdGeneratorUtil.gen())
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
    void givenAuthMail_whenFindById_thenReturnValid() {
        // Given
        String email = "testEmail@test.com";
        String code = "1t2e3s4t";
        MailStatus mailStatus = MailStatus.N;
        AuthMailEntity authMail = AuthMailEntity
                .builder()
                .id(LongIdGeneratorUtil.gen())
                .email(email)
                .code(code)
                .mailStatus(mailStatus)
                .build();

        AuthMailEntity saveAuthMailEntity = authMailRepository.save(authMail);

        // When
        AuthMailEntity foundAuthMail = authMailRepository.findById(saveAuthMailEntity.getId()).orElse(null);

        // Then
        assertNotNull(foundAuthMail);
        assertEquals(authMail.getEmail(), foundAuthMail.getEmail());
        assertEquals(authMail.getCode(), foundAuthMail.getCode());
    }

    @Test
    void givenAuthMail_whenFindByEmail_thenReturnValid() {
        // Given
        String email = "testEmail@test.com";
        String code = "1t2e3s4t";
        MailStatus mailStatus = MailStatus.N;
        AuthMailEntity authMail = AuthMailEntity
                .builder()
                .id(LongIdGeneratorUtil.gen())
                .email(email)
                .code(code)
                .mailStatus(mailStatus)
                .build();

        authMailRepository.save(authMail);

        // When
        AuthMailEntity foundAuthMail = authMailRepository.findByEmail(authMail.getEmail()).orElse(null);

        // Then
        assertNotNull(foundAuthMail);
        assertEquals(authMail.getEmail(), foundAuthMail.getEmail());
        assertEquals(authMail.getCode(), foundAuthMail.getCode());
    }

    @Test
    void givenAuthMail_whenFindByEmailAndCode_thenReturnValid() {
        // Given
        String email = "testEmail@test.com";
        String code = "1t2e3s4t";
        MailStatus mailStatus = MailStatus.N;
        AuthMailEntity authMail = AuthMailEntity
                .builder()
                .id(LongIdGeneratorUtil.gen())
                .email(email)
                .code(code)
                .mailStatus(mailStatus)
                .build();

        AuthMailEntity saveAuthMailEntity = authMailRepository.save(authMail);

        // When
        AuthMailEntity foundAuthMail = authMailRepository.findByEmailAndCode(saveAuthMailEntity.getEmail(), saveAuthMailEntity.getCode()).orElse(null);

        // Then
        assertNotNull(foundAuthMail);
        assertEquals(authMail.getEmail(), foundAuthMail.getEmail());
        assertEquals(authMail.getCode(), foundAuthMail.getCode());
    }

    @Test
    void givenAuthMail_whenSaved_thenReturnValid() {
        // Given
        String email = "testEmail@test.com";
        String code = "1t2e3s4t";
        MailStatus mailStatus = MailStatus.N;
        AuthMailEntity authMail = AuthMailEntity
                .builder()
                .id(LongIdGeneratorUtil.gen())
                .email(email)
                .code(code)
                .mailStatus(mailStatus)
                .build();

        // When
        AuthMailEntity saveAuthMail = authMailRepository.save(authMail);

        // Then
        AuthMailEntity  foundAuthMail = authMailRepository.findById(saveAuthMail.getId()).orElse(null);
        assertNotNull(foundAuthMail);
        assertEquals(saveAuthMail.getEmail(), foundAuthMail.getEmail());
        assertEquals(saveAuthMail.getCode(), foundAuthMail.getCode());
    }
}
