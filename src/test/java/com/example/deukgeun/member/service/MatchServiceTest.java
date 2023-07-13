package com.example.deukgeun.member.service;

import com.example.deukgeun.global.entity.MatchInfo;
import com.example.deukgeun.global.repository.MatchInfoRepository;
import com.example.deukgeun.member.request.SaveMatchInfoRequest;
import com.example.deukgeun.member.service.implement.MatchServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class MatchServiceTest {

    @InjectMocks
    private MatchServiceImpl matchService;
    @Mock
    private MatchInfoRepository matchInfoRepository;

    @Test
    void givenSaveMatchInfoRequest_whenSave_thenIsSaved() {
        // Given
        SaveMatchInfoRequest saveMatchInfoRequest = new SaveMatchInfoRequest();
        saveMatchInfoRequest.setApplicantId(123L);
        saveMatchInfoRequest.setJobPostingId(123L);

        // When
        matchService.save(saveMatchInfoRequest);

        // Then
        verify(matchInfoRepository, times(1)).save(any(MatchInfo.class));
    }

    @Test
    void givenExistingApplicantId_whenDeleteByApplicantId_thenIsDeleted() {
        // Given
        Long applicantId = 123L;

        // When
        matchService.deleteByApplicantId(applicantId);

        // Then
        verify(matchInfoRepository, times(1)).deleteByApplicantId(applicantId);
    }


    @Test
    public void givenExistingJobPostingId_whenIsAnnouncementMatchedByJobPostingId_thenShouldThrowEntityExistsException() {
        // Given
        Long jobPostingId = 12345L;
        given(matchInfoRepository.existsByJobPostingId(jobPostingId)).willReturn(true);

        // When and Then
        assertThrows(EntityExistsException.class, () -> {
            matchService.isAnnouncementMatchedByJobPostingId(jobPostingId);
        });

        verify(matchInfoRepository, times(1)).existsByJobPostingId(jobPostingId);
    }

    @Test
    public void givenNotExistingJobPostingId_whenIsAnnouncementMatchedByJobPostingId_thenShouldNotThrowEntityExistsException() {
        // Given
        Long jobPostingId = 12345L;
        given(matchInfoRepository.existsByJobPostingId(jobPostingId)).willReturn(false);

        // When and Then
        assertDoesNotThrow(() -> {
            matchService.isAnnouncementMatchedByJobPostingId(jobPostingId);
        });

        verify(matchInfoRepository, times(1)).existsByJobPostingId(jobPostingId);
    }
}
