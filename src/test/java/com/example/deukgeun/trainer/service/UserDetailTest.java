package com.example.deukgeun.trainer.service;

import com.example.deukgeun.trainer.entity.Member;
import com.example.deukgeun.trainer.repository.MemberRepository;
import com.example.deukgeun.trainer.service.implement.UserDetailServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class UserDetailTest {
    @InjectMocks
    private UserDetailServiceImpl userDetailService;
    @Mock
    private MemberRepository memberRepository;

    @Test
    public void givenExistingEmail_whenLoadUserByUsername_thenUserDetailsIsReturned() {
        // Given
        String email = "test@example.com";
        Member member = Member
                .builder()
                .id(1L)
                .email(email)
                .build();

        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

        // When
        UserDetails userDetails = userDetailService.loadUserByUsername(email);

        // Then
        assertEquals(member.getEmail(), userDetails.getUsername());
    }

    @Test
    public void givenNonExistingEmail_whenLoadUserByUsername_thenUsernameNotFoundExceptionIsThrown() {
        // Given
        String email = "test@example.com";

        // Mocking the behavior of userRepository.findByEmail()
        given(memberRepository.findByEmail(email)).willReturn(Optional.empty());

        // When/Then
        assertThrows(UsernameNotFoundException.class, () -> userDetailService.loadUserByUsername(email));
    }

}
