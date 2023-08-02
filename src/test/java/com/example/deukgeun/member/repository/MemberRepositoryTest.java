package com.example.deukgeun.member.repository;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.member.infrastructure.persistence.entity.MemberEntity;
import com.example.deukgeun.member.infrastructure.persistence.repository.MemberJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {
    @Autowired
    private MemberJpaRepository memberRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(memberRepository);
    }

    @Test
    void givenMember_whenSaved_thenReturnValid() {
        // Given
        MemberEntity memberEntity = MemberEntity
                .builder()
                .id(123L)
                .age(23)
                .gender(Gender.M)
                .name("test")
                .email("Test")
                .password("test")
                .build();

        // When
        MemberEntity saveMemberEntity = memberRepository.save(memberEntity);

        // Then
        MemberEntity foundMemberEntity = memberRepository.findById(saveMemberEntity.getId()).orElse(null);
        assertNotNull(foundMemberEntity);
        assertEquals(saveMemberEntity.getEmail(), foundMemberEntity.getEmail());
        assertEquals(saveMemberEntity.getName(), foundMemberEntity.getName());
    }
    @Test
    void givenTrainer_whenFindByEmail_thenReturnValid() {
        // Given
        String email = "test";
        MemberEntity memberEntity = MemberEntity
                .builder()
                .id(123L)
                .age(23)
                .gender(Gender.M)
                .name("test")
                .email("test")
                .password("test")
                .build();

        memberRepository.save(memberEntity);

        // When
        Optional<MemberEntity> foundMemberEntity = memberRepository.findByEmail(email);

        // Then
        assertTrue(foundMemberEntity.isPresent());
        assertEquals(email, foundMemberEntity.get().getEmail());
    }
}
