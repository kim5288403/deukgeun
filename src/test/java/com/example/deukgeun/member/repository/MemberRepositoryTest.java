package com.example.deukgeun.member.repository;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        Member member = Member
                .builder()
                .id(123L)
                .email("test")
                .password("1234")
                .name("test")
                .age(123)
                .gender(Gender.M)
                .build();

        // When
        Member saveMember = memberRepository.save(member);

        // Then
        Member foundMember = memberRepository.findById(saveMember.getId()).orElse(null);
        assertNotNull(foundMember);
        assertEquals(saveMember.getEmail(), foundMember.getEmail());
        assertEquals(saveMember.getName(), foundMember.getName());
    }
}
