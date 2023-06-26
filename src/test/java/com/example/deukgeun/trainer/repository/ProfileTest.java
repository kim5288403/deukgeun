package com.example.deukgeun.trainer.repository;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.Member;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.response.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProfileTest {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MemberRepository memberRepository;

    private long memberId;

    @BeforeEach
    void setUp() {
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

        Member saveMember = memberRepository.save(member);
        memberId = saveMember.getId();
    }

    @Test
    void shouldNotNullRepository() {
        assertNotNull(profileRepository);
    }

    @Test
    void givenProfile_whenSaved_thenReturnValid() {
        // Given
        Profile profile = Profile
                .builder()
                .memberId(memberId)
                .path("test")
                .build();

        // When
        Profile saveProfile = profileRepository.save(profile);

        // Then
        Profile foundProfile = profileRepository.findById(saveProfile.getId()).orElse(null);
        assertNotNull(foundProfile);
        assertEquals(saveProfile.getPath(), foundProfile.getPath());
    }

    @Test
    void givenProfile_whenFindByMemberId_thenReturnValid() {
        // Given
        Profile profile = Profile
                .builder()
                .memberId(memberId)
                .path("test")
                .build();
        profileRepository.save(profile);

        // When
        Profile foundProfile = profileRepository.findByMemberId(memberId).orElse(null);

        // Then
        assertNotNull(foundProfile);
        assertEquals(profile.getPath(), foundProfile.getPath());
    }

    @Test
    @Sql(scripts = "classpath:profileFindByUserLikeKeywordSet.sql")
    void givenPosts_whenFindByUserLikeKeyword_thenReturnValid() {
        // Given
        String keyword = "test";
        String converterKeyword = "%" + keyword +"%";
        PageRequest pageable = PageRequest.of(0, 10);

        // When
        Page<MemberResponse.MemberListResponse> foundPost = profileRepository.findByUserLikeKeyword(converterKeyword, pageable);

        // Then
        assertEquals(keyword, foundPost.getContent().get(0).getName());
        assertEquals(keyword, foundPost.getContent().get(1).getName());
        assertNotEquals(foundPost.getContent().get(0).getPath(), foundPost.getContent().get(1).getPath());
    }

    @Test
    void givenProfile_whenUpdatePath_thenIsUpdated() {
        // Given
        String newPath = "newPath";
        Profile profile = Profile
                .builder()
                .memberId(memberId)
                .path("test")
                .build();
        Profile saveProfile = profileRepository.save(profile);
        Profile foundProfile = profileRepository.findById(saveProfile.getId()).orElse(null);

        // When
        assert foundProfile != null;
        foundProfile.updatePath(newPath);

        // Then
        assertEquals(newPath, foundProfile.getPath());
    }


}
