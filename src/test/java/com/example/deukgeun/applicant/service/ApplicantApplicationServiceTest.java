package com.example.deukgeun.applicant.service;

import com.example.deukgeun.applicant.application.dto.request.PaymentInfoRequest;
import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.applicant.application.dto.response.ApplicantResponse;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.application.service.implement.ApplicantApplicationServiceImpl;
import com.example.deukgeun.applicant.domain.dto.PaymentCancelInfoDTO;
import com.example.deukgeun.applicant.domain.dto.SaveApplicantDTO;
import com.example.deukgeun.applicant.domain.dto.SaveMatchInfoDTO;
import com.example.deukgeun.applicant.domain.dto.SavePaymentInfoDTO;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.service.ApplicantDomainService;
import com.example.deukgeun.applicant.infrastructure.persistence.mapper.ApplicantMapper;
import com.example.deukgeun.applicant.infrastructure.persistence.mapper.MatchMapper;
import com.example.deukgeun.applicant.infrastructure.persistence.mapper.PaymentMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ApplicantApplicationServiceTest {
    @InjectMocks
    private ApplicantApplicationServiceImpl applicantApplicationService;
    @Mock
    private ApplicantDomainService applicantDomainService;
    @Mock
    private ApplicantMapper applicantMapper;

    @Test
    public void givenValidId_whenFindById_thenReturnFoundApplicant() {
        // Given
        Long applicantId = 1L;
        Applicant existingApplicant = mock(Applicant.class);

        given(applicantDomainService.findById(anyLong())).willReturn(existingApplicant);

        // When
        Applicant applicant = applicantApplicationService.findById(applicantId);

        // Then
        assertNotNull(applicant);
        verify(applicantDomainService, times(1)).findById(anyLong());
    }

    @Test
    public void givenInValidId_whenFindById_thenEntityNotFoundException() {
        // Given
        Long applicantId = 999L;

        given(applicantDomainService.findById(anyLong())).willThrow(EntityNotFoundException.class);

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> {
            applicantApplicationService.findById(applicantId);
        });
    }

    @Test
    public void givenValidJobIdAndCurrentPage_whenGetListByJobId_thenReturnApplicantListResponsePage() {
        // Given
        Long jobId = 1L;
        Long trainerId = 1L;
        int supportAmount = 10000;
        int currentPage = 0;
        int isSelected = 0;
        PageRequest pageRequest = PageRequest.of(currentPage, 10);
        List<Applicant> applicantsList = new ArrayList<>();
        applicantsList.add(Applicant.create(jobId, trainerId, supportAmount, isSelected));
        applicantsList.add(Applicant.create(jobId, trainerId, supportAmount, isSelected));
        Page<Applicant> applicantsPage = new PageImpl<>(applicantsList, pageRequest, applicantsList.size());

        ApplicantResponse.List applicantResponseList = new ApplicantResponse.List();
        applicantResponseList.setId(applicantsList.get(0).getId());
        applicantResponseList.setJobId(jobId);
        applicantResponseList.setTrainerId(trainerId);
        applicantResponseList.setSupportAmount(supportAmount);
        applicantResponseList.setIsSelected(isSelected);

        given(applicantDomainService.findPageByJobId(anyLong(), any(PageRequest.class))).willReturn(applicantsPage);
        given(applicantMapper.toApplicantResponseList(any(Applicant.class))).willReturn(applicantResponseList);

        // When
        Page<ApplicantResponse.List> applicantResponsePage = applicantApplicationService.getListByJobId(jobId, currentPage);

        // Then
        assertEquals(applicantsList.size(), applicantResponsePage.getContent().size());
        assertEquals(applicantsList.get(0).getId(), applicantResponsePage.getContent().get(0).getId());
        assertEquals(applicantsList.get(0).getTrainerId(), applicantResponsePage.getContent().get(0).getTrainerId());
        assertEquals(applicantsList.get(0).getSupportAmount(), applicantResponsePage.getContent().get(0).getSupportAmount());
        verify(applicantDomainService, times(1)).findPageByJobId(anyLong(), any(PageRequest.class));
    }

    @Test
    public void givenValidSaveApplicantRequestAndTrainerId_whenSave_thenReturnSavedApplicant() {
        // Given
        Long trainerId = 1L;
        SaveApplicantDTO saveApplicantDTO = mock(SaveApplicantDTO.class);
        SaveApplicantRequest saveApplicantRequest = new SaveApplicantRequest(123L, 1000);
        Applicant expectedSavedApplicant = Applicant.create(saveApplicantRequest.getJobId(), trainerId, saveApplicantRequest.getSupportAmount(), 0);

        given(applicantMapper.toSaveApplicantDto(anyLong(), any(SaveApplicantRequest.class))).willReturn(saveApplicantDTO);
        given(applicantDomainService.save(any(SaveApplicantDTO.class))).willReturn(expectedSavedApplicant);

        // When
        Applicant savedApplicant = applicantApplicationService.save(saveApplicantRequest, trainerId);

        // Then
        assertEquals(expectedSavedApplicant.getId(), savedApplicant.getId());
        assertEquals(expectedSavedApplicant.getJobId(), savedApplicant.getJobId());
        assertEquals(expectedSavedApplicant.getTrainerId(), savedApplicant.getTrainerId());
        assertEquals(expectedSavedApplicant.getSupportAmount(), savedApplicant.getSupportAmount());
        verify(applicantDomainService,times(1)).save(any(SaveApplicantDTO.class));
    }

    @Test
    public void givenValidIdAndIsSelected_whenUpdateIsSelectedById_thenUpdateIsSelectedByIdCalled() {
        // Given
        Long applicantId = 1L;
        int isSelected = 1;

        // When
        applicantApplicationService.updateIsSelectedById(applicantId, isSelected);

        // Then
        verify(applicantDomainService).updateIsSelectedById(anyLong(), anyInt());
    }
}
