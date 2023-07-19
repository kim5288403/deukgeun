package com.example.deukgeun.member.repository;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.member.domain.entity.Member;
import com.example.deukgeun.member.domain.repository.MemberRepository;
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
    private MemberRepository memberRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(memberRepository);
    }

    @Test
    void givenMember_whenSaved_thenReturnValid() {
        // Given
        Member member = Member.create(
                "test",
                "test",
                "test",
                23,
                Gender.M
        );

        // When
        Member saveMember = memberRepository.save(member);

        // Then
        Member foundMember = memberRepository.findById(saveMember.getId()).orElse(null);
        assertNotNull(foundMember);
        assertEquals(saveMember.getEmail(), foundMember.getEmail());
        assertEquals(saveMember.getName(), foundMember.getName());
    }
    @Test
    void givenTrainer_whenFindByEmail_thenReturnValid() {
        // Given
        String email = "test";
        Member member = Member.create(
                "test",
                "test",
                "test",
                23,
                Gender.M
        );

        memberRepository.save(member);

        // When
        Optional<Member> foundMember = memberRepository.findByEmail(email);

        // Then
        assertTrue(foundMember.isPresent());
        assertEquals(email, foundMember.get().getEmail());
    }
}
