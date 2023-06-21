package com.example.deukgeun.trainer.repository;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.Member;
import com.example.deukgeun.trainer.entity.Post;
import org.junit.jupiter.api.BeforeEach;
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
public class PostTest {

    @Autowired
    private PostRepository postRepository;
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
        assertNotNull(postRepository);
    }

    @Test
    void givenPost_whenSaved_thenReturnValid() {
        // Given
        Post post = Post
                .builder()
                .html("test")
                .memberId(memberId)
                .build();

        // When
        Post savePost = postRepository.save(post);

        // Then
        Post foundPost = postRepository.findById(savePost.getId()).orElse(null);
        assertNotNull(foundPost);
        assertEquals(savePost.getHtml(), foundPost.getHtml());
    }

    @Test
    void givenPost_whenFindByMemberId_thenReturnValid() {
        // Given
        Post post = Post
                .builder()
                .html("test")
                .memberId(memberId)
                .build();
        postRepository.save(post);

        // When
        Post foundPost = postRepository.findByMemberId(memberId).orElse(null);

        // Then
        assertNotNull(foundPost);
        assertEquals(post.getHtml(), foundPost.getHtml());
    }

    @Test
    void givenPost_whenUpdateHtml_thenIsUpdated() {
        // Given
        String html = "html";
        String newHtml = "newHtml";

        Post post = Post
                .builder()
                .html(html)
                .memberId(memberId)
                .build();

        postRepository.save(post);
        Post foundPost = postRepository.findByMemberId(memberId).orElse(null);

        // When
        assert foundPost != null;
        foundPost.updateHtml(newHtml);
        postRepository.save(foundPost);

        // Then
        assertNotNull(foundPost);
        assertEquals(newHtml, foundPost.getHtml());
        assertNotEquals(html, foundPost.getHtml());
    }

}
