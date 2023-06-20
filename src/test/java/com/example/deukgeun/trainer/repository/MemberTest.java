package com.example.deukgeun.trainer.repository;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.Member;
import com.example.deukgeun.trainer.request.UpdateInfoRequest;
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
public class MemberTest {
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
                .name("테스트")
                .email("testEmail@test.com")
                .password("test1234!")
                .gender(Gender.F)
                .groupStatus(GroupStatus.Y)
                .groupName("test")
                .introduction("test")
                .detailAddress("test")
                .extraAddress("test")
                .jibunAddress("test")
                .roadAddress("test")
                .postcode("test")
                .price(3000)
                .build();

        // When
        Member saveMember = memberRepository.save(member);

        // Then
        Member foundMember = memberRepository.findById(saveMember.getId()).orElse(null);
        assertNotNull(foundMember);
        assertEquals(saveMember.getEmail(), foundMember.getEmail());
        assertEquals(saveMember.getName(), foundMember.getName());
    }

    @Test
    void givenMember_whenFindByEmail_thenReturnValid() {
        // Given
        String email = "testEmail@test.com";
        Member member = Member
                .builder()
                .name("테스트")
                .email(email)
                .password("test1234!")
                .gender(Gender.F)
                .groupStatus(GroupStatus.Y)
                .groupName("test")
                .introduction("test")
                .detailAddress("test")
                .extraAddress("test")
                .jibunAddress("test")
                .roadAddress("test")
                .postcode("test")
                .price(3000)
                .build();
        memberRepository.save(member);

        // When
        Optional<Member> foundMember = memberRepository.findByEmail(email);

        // Then
        assertTrue(foundMember.isPresent());
        assertEquals(email, foundMember.get().getEmail());
    }

    @Test
    void givenMember_whenExistsByEmail_thenReturnTrue() {
        // Given
        String email = "testEmail@test.com";
        Member member = Member.builder()
                .name("테스트")
                .email(email)
                .password("test1234!")
                .gender(Gender.F)
                .groupStatus(GroupStatus.Y)
                .groupName("test")
                .introduction("test")
                .detailAddress("test")
                .extraAddress("test")
                .jibunAddress("test")
                .roadAddress("test")
                .postcode("test")
                .price(3000)
                .build();
        memberRepository.save(member);

        // When
        boolean existsByEmail = memberRepository.existsByEmail(email);

        // Then
        assertTrue(existsByEmail);
    }

    @Test
    void givenMember_whenUpdateInfo_thenIsUpdated() {
        // Given
        String email = "testEmail@test.com";
        Member member = Member
                .builder()
                .name("테스트")
                .email(email)
                .password("test1234!")
                .gender(Gender.F)
                .groupStatus(GroupStatus.Y)
                .groupName("test")
                .introduction("test")
                .detailAddress("test")
                .extraAddress("test")
                .jibunAddress("test")
                .roadAddress("test")
                .postcode("test")
                .price(3000)
                .build();
        memberRepository.save(member);
        Member foundMember = memberRepository.findByEmail(email).orElse(null);

        UpdateInfoRequest updateInfoRequest = new UpdateInfoRequest();
        updateInfoRequest.setEmail("updateTestEmail@test.com");
        updateInfoRequest.setName("test");
        updateInfoRequest.setGender(Gender.F);
        updateInfoRequest.setPostcode("test");
        updateInfoRequest.setJibunAddress("test");
        updateInfoRequest.setRoadAddress("test");
        updateInfoRequest.setDetailAddress("test");
        updateInfoRequest.setExtraAddress("test");
        updateInfoRequest.setPrice(10000);
        updateInfoRequest.setGroupStatus(GroupStatus.Y);
        updateInfoRequest.setGroupName("test");
        updateInfoRequest.setIntroduction("test");

        // When
        assert foundMember != null;
        foundMember.updateInfo(updateInfoRequest);
        memberRepository.save(foundMember);

        // Then
        Member resultMember = memberRepository.findById(foundMember.getId()).orElse(null);
        assertNotNull(resultMember);
        assertNotEquals(email, resultMember.getEmail());
    }

    @Test
    void givenMember_whenUpdatePassword_thenIsUpdated() {
        // Given
        String email = "testEmail@test.com";
        String password = "testPassword";
        Member member = Member
                .builder()
                .name("테스트")
                .email(email)
                .password(password)
                .gender(Gender.F)
                .groupStatus(GroupStatus.Y)
                .groupName("test")
                .introduction("test")
                .detailAddress("test")
                .extraAddress("test")
                .jibunAddress("test")
                .roadAddress("test")
                .postcode("test")
                .price(3000)
                .build();
        memberRepository.save(member);
        Member foundMember = memberRepository.findByEmail(email).orElse(null);

        // When
        assert foundMember != null;
        foundMember.updatePassword("newPasswordTest");
        memberRepository.save(foundMember);

        // Then
        Member resultMember = memberRepository.findById(foundMember.getId()).orElse(null);
        assertNotNull(resultMember);
        assertNotEquals(password, resultMember.getPassword());
    }

}
