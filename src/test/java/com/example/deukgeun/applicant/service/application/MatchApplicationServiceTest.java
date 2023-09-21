package com.example.deukgeun.applicant.service.application;

import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.applicant.application.service.implement.MatchApplicationServiceImpl;
import com.example.deukgeun.applicant.domain.dto.SaveMatchInfoDTO;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.service.ApplicantDomainService;
import com.example.deukgeun.applicant.infrastructure.persistence.mapper.MatchMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MatchApplicationServiceTest {
    @InjectMocks
    private MatchApplicationServiceImpl matchApplicationService;
    @Mock
    private ApplicantDomainService applicantDomainService;
    @Mock
    private MatchMapper matchMapper;

    @Test
    public void givenValidId_whenDeleteMatchInfoById_thenDeleteMatchInfoByIdCalled() {
        // Given
        Long applicantId = 1L;

        // When
        matchApplicationService.deleteMatchInfoById(applicantId);

        // Then
        verify(applicantDomainService, times(1)).deleteMatchInfoById(anyLong());
    }

    @Test
    public void givenValidJobId_whenIsAnnouncementMatchedByJobId_thenNoExceptionThrown() {
        // Given
        Long jobId = 1L;

        given(applicantDomainService.isAnnouncementMatchedByJobId(anyLong())).willReturn(false);

        // When and Then
        assertDoesNotThrow(() -> matchApplicationService.isAnnouncementMatchedByJobId(jobId));
        verify(applicantDomainService, times(1)).isAnnouncementMatchedByJobId(anyLong());
    }

    @Test
    public void givenInValidJobId_whenIsAnnouncementMatchedByJobId_thenEntityExistsExceptionThrown() {
        // Given
        Long jobId = 1L;

        given(applicantDomainService.isAnnouncementMatchedByJobId(anyLong())).willReturn(true);

        // When and Then
        assertThrows(EntityExistsException.class, () -> matchApplicationService.isAnnouncementMatchedByJobId(jobId));
        verify(applicantDomainService, times(1)).isAnnouncementMatchedByJobId(anyLong());
    }

    @Test
    public void givenValidSaveMatchInfoRequest_whenSaveMatchInfo_thenReturnSavedApplicant() {
        // Given
        int PAYMENT_WAITING = 1;
        SaveMatchInfoRequest saveMatchInfoRequest = mock(SaveMatchInfoRequest.class);
        SaveMatchInfoDTO saveMatchInfoDTO = mock(SaveMatchInfoDTO.class);
        Applicant applicant = mock(Applicant.class);

        given(matchMapper.toSaveMatchInfoDto(anyInt(), any(SaveMatchInfoRequest.class))).willReturn(saveMatchInfoDTO);
        given(applicantDomainService.saveMatchInfo(any(SaveMatchInfoDTO.class))).willReturn(applicant);

        // When
        Applicant matchedApplicant = matchApplicationService.saveMatchInfo(saveMatchInfoRequest, PAYMENT_WAITING);

        // Then
        assertEquals(applicant, matchedApplicant);
        verify(applicantDomainService).saveMatchInfo(any(SaveMatchInfoDTO.class));
    }


}
