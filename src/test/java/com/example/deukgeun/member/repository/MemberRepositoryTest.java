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
    void givenValidMember_whenSave_thenMemberIsSaved() {
        // Given
        MemberEntity memberEntity = MemberEntity
                .builder()
                .id(1L)
                .age(20)
                .gender(Gender.M)
                .name("name")
                .email("email")
                .password("password")
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
    void givenValidEmail_whenFindByEmail_thenReturnFoundIsMember() {
        // Given
        String email = "email";
        MemberEntity memberEntity = MemberEntity
                .builder()
                .id(1L)
                .age(20)
                .gender(Gender.M)
                .name("name")
                .email(email)
                .password("password")
                .build();

        memberRepository.save(memberEntity);

        // When
        Optional<MemberEntity> foundMemberEntity = memberRepository.findByEmail(email);

        // Then
        assertTrue(foundMemberEntity.isPresent());
        assertEquals(email, foundMemberEntity.get().getEmail());
    }
}
