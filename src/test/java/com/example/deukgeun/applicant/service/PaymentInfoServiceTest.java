package com.example.deukgeun.applicant.service;

import com.example.deukgeun.applicant.domain.model.entity.PaymentInfo;
import com.example.deukgeun.applicant.infrastructure.persistence.repository.PaymentInfoJpaRepository;
import com.example.deukgeun.applicant.application.dto.request.PaymentInfoRequest;
import com.example.deukgeun.applicant.infrastructure.persistence.PaymentInfoServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PaymentInfoServiceTest {

    @InjectMocks
    private PaymentInfoServiceImpl paymentInfoService;
    @Mock
    private PaymentInfoJpaRepository paymentInfoRepository;

    @Test
    public void givenPaymentInfoRequest_whenSave_thenShouldReturnSavedPaymentInfo() {
        // Given
        PaymentInfoRequest request = new PaymentInfoRequest();
        request.setApplicantId(12345L);
        request.setImpUid("imp12345");
        request.setPgProvider("pg-provider");
        request.setPgTid("pg-tid");
        request.setChannel("channel");
        request.setAmount(1000);
        request.setPaidAt("2023-07-14T10:30:00.000+00:00");

        PaymentInfo expectedPaymentInfo = new PaymentInfo();
        given(paymentInfoRepository.save(any(PaymentInfo.class))).willReturn(expectedPaymentInfo);

        // When
        PaymentInfo result = paymentInfoService.save(request);

        // Then
        assertNotNull(result);
        assertEquals(expectedPaymentInfo, result);
        verify(paymentInfoRepository, times(1)).save(any(PaymentInfo.class));
    }

    @Test
    public void givenExistingApplicantId_whenGetPaymentInfoByApplicantId_thenReturnPaymentInfo() {
        // Given
        Long applicantId = 1L;
        PaymentInfo expectedPaymentInfo = PaymentInfo
                .builder()
                .applicantId(applicantId)
                .build();
        given(paymentInfoRepository.findByApplicantIdAndDeleteDateIsNull(applicantId)).willReturn(Optional.of(expectedPaymentInfo));

        // When
        PaymentInfo actualPaymentInfo = paymentInfoService.getPaymentInfoByApplicantId(applicantId);

        // Then
        assertNotNull(actualPaymentInfo);
        assertEquals(expectedPaymentInfo.getApplicantId(), actualPaymentInfo.getApplicantId());
        verify(paymentInfoRepository, times(1)).findByApplicantIdAndDeleteDateIsNull(applicantId);
    }

    @Test
    public void givenNonExistingApplicantId_whenGetPaymentInfoByApplicantId_thenThrowEntityNotFoundException() {
        // Given
        Long applicantId = 2L;
        given(paymentInfoRepository.findByApplicantIdAndDeleteDateIsNull(applicantId)).willReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> paymentInfoService.getPaymentInfoByApplicantId(applicantId));
    }

    @Test
    public void givenExistingImpUid_whenDeleteByImpUid_thenPaymentInfoIsDeleted() {
        // Given
        String impUid = "sample_imp_uid";
        PaymentInfo paymentInfo = new PaymentInfo();
        given(paymentInfoRepository.findByImpUid(impUid)).willReturn(Optional.of(paymentInfo));

        // When
        paymentInfoService.deleteByImpUid(impUid);

        // Then
        verify(paymentInfoRepository, times(1)).findByImpUid(impUid);
        verify(paymentInfoRepository, times(1)).save(paymentInfo);
    }

    @Test
    public void givenNonExistingImpUid_whenDeleteByImpUid_thenThrowEntityNotFoundException() {
        // Given
        String impUid = "non_existing_imp_uid";
        given(paymentInfoRepository.findByImpUid(impUid)).willReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> paymentInfoService.deleteByImpUid(impUid));
        verify(paymentInfoRepository, times(1)).findByImpUid(impUid);
        verify(paymentInfoRepository, never()).save(any(PaymentInfo.class));
    }

}
