package com.example.deukgeun.member.service;

import com.example.deukgeun.global.entity.MatchInfo;
import com.example.deukgeun.global.repository.MatchInfoRepository;
import com.example.deukgeun.member.request.SaveMatchInfoRequest;
import com.example.deukgeun.member.service.implement.MatchInfoServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class MatchInfoServiceTest {

    @InjectMocks
    private MatchInfoServiceImpl matchInfoService;
    @Mock
    private MatchInfoRepository matchInfoRepository;

    @Test
    void givenSaveMatchInfoRequest_whenSave_thenIsSaved() {
        // Given
        SaveMatchInfoRequest saveMatchInfoRequest = new SaveMatchInfoRequest();
        saveMatchInfoRequest.setApplicantId(123L);
        saveMatchInfoRequest.setJobPostingId(123L);

        given(matchInfoRepository.existsByJobPostingId(saveMatchInfoRequest.getJobPostingId())).willReturn(false);

        // When
        matchInfoService.save(saveMatchInfoRequest);

        // Then
        verify(matchInfoRepository, times(1)).save(any(MatchInfo.class));
    }

    @Test
    void givenExistingApplicantId_whenDeleteByApplicantId_thenIsDeleted() {
        // Given
        Long applicantId = 123L;

        // When
        matchInfoService.deleteByApplicantId(applicantId);

        // Then
        verify(matchInfoRepository, times(1)).deleteByApplicantId(applicantId);
    }
}
