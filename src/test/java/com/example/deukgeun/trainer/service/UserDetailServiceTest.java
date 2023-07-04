package com.example.deukgeun.trainer.service;

import com.example.deukgeun.trainer.entity.Trainer;
import com.example.deukgeun.trainer.repository.TrainerRepository;
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
public class UserDetailServiceTest {
    @InjectMocks
    private UserDetailServiceImpl userDetailService;
    @Mock
    private TrainerRepository trainerRepository;

    @Test
    public void givenExistingEmail_whenLoadUserByUsername_thenUserDetailsIsReturned() {
        // Given
        String email = "test@example.com";
        Trainer trainer = Trainer
                .builder()
                .id(1L)
                .email(email)
                .build();

        given(trainerRepository.findByEmail(email)).willReturn(Optional.of(trainer));

        // When
        UserDetails userDetails = userDetailService.loadUserByUsername(email);

        // Then
        assertEquals(trainer.getEmail(), userDetails.getUsername());
    }

    @Test
    public void givenNonExistingEmail_whenLoadUserByUsername_thenUsernameNotFoundExceptionIsThrown() {
        // Given
        String email = "test@example.com";

        // Mocking the behavior of userRepository.findByEmail()
        given(trainerRepository.findByEmail(email)).willReturn(Optional.empty());

        // When/Then
        assertThrows(UsernameNotFoundException.class, () -> userDetailService.loadUserByUsername(email));
    }

}
