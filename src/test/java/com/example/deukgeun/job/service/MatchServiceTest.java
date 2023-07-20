package com.example.deukgeun.job.service;

import com.example.deukgeun.job.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.job.application.service.implement.MatchServiceImpl;
import com.example.deukgeun.job.domain.entity.MatchInfo;
import com.example.deukgeun.job.domain.repository.MatchInfoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
        MatchInfo matchInfo = mock(MatchInfo.class);
        given(matchInfoRepository.findByApplicantIdAndDeleteDateIsNull(anyLong())).willReturn(Optional.ofNullable(matchInfo));

        // When
        matchService.deleteByApplicantId(applicantId);

        // Then
        assert matchInfo != null;
        verify(matchInfoRepository, times(1)).save(matchInfo);
    }


    @Test
    public void givenExistingJobPostingId_whenIsAnnouncementMatchedByJobPostingId_thenShouldThrowEntityExistsException() {
        // Given
        Long jobPostingId = 12345L;
        given(matchInfoRepository.existsByJobPostingIdAndDeleteDateIsNull(jobPostingId)).willReturn(true);

        // When and Then
        assertThrows(EntityExistsException.class, () -> {
            matchService.isAnnouncementMatchedByJobPostingId(jobPostingId);
        });

        verify(matchInfoRepository, times(1)).existsByJobPostingIdAndDeleteDateIsNull(jobPostingId);
    }

    @Test
    public void givenNotExistingJobPostingId_whenIsAnnouncementMatchedByJobPostingId_thenShouldNotThrowEntityExistsException() {
        // Given
        Long jobPostingId = 12345L;
        given(matchInfoRepository.existsByJobPostingIdAndDeleteDateIsNull(jobPostingId)).willReturn(false);

        // When and Then
        assertDoesNotThrow(() -> {
            matchService.isAnnouncementMatchedByJobPostingId(jobPostingId);
        });

        verify(matchInfoRepository, times(1)).existsByJobPostingIdAndDeleteDateIsNull(jobPostingId);
    }
}
