package com.example.deukgeun.applicant.service;

import com.example.deukgeun.applicant.application.dto.request.PaymentInfoRequest;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.application.service.implement.PaymentApplicationServiceImpl;
import com.example.deukgeun.applicant.domain.dto.PaymentCancelInfoDTO;
import com.example.deukgeun.applicant.domain.dto.SavePaymentInfoDTO;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.service.ApplicantDomainService;
import com.example.deukgeun.applicant.infrastructure.persistence.mapper.PaymentMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest

public class PaymentApplicationServiceTest {
    @InjectMocks
    private PaymentApplicationServiceImpl paymentApplicationService;

    @Mock
    private ApplicantDomainService applicantDomainService;
    @Mock
    private PaymentMapper paymentMapper;

    @Test
    public void givenValidPaymentInfoRequest_whenSavePaymentInfo_thenSavePaymentInfoCalled() {
        // Given
        PaymentInfoRequest paymentInfoRequest = new PaymentInfoRequest();
        paymentInfoRequest.setPaidAt("2022-12-15T13:00:00.123+09:00");
        SavePaymentInfoDTO savePaymentInfoDTO = mock(SavePaymentInfoDTO.class);
        given(paymentMapper.toSavePaymentInfoDto(any(LocalDateTime.class), any(PaymentInfoRequest.class))).willReturn(savePaymentInfoDTO);

        // When
        paymentApplicationService.savePaymentInfo(paymentInfoRequest);

        // Then
        verify(applicantDomainService).savePaymentInfo(any(SavePaymentInfoDTO.class));
    }

    @Test
    public void givenIdAndIamPortCancelResponse_whenUpdatePaymentCancelInfoById_thenShouldUpdatePaymentInfo() {
        // Given
        Long applicantId = 1L;
        Applicant applicant = mock(Applicant.class);
        IamPortCancelResponse iamPortCancelResponse = mock(IamPortCancelResponse.class);
        PaymentCancelInfoDTO paymentCancelInfoDTO = mock(PaymentCancelInfoDTO.class);

        given(applicantDomainService.findById(applicantId)).willReturn(applicant);
        given(paymentMapper.toPaymentCancelInfoDto(anyLong(), any(IamPortCancelResponse.class))).willReturn(paymentCancelInfoDTO);

        // When
        paymentApplicationService.updatePaymentCancelInfoById(applicantId, iamPortCancelResponse);

        // Then
        verify(applicantDomainService).updatePaymentCancelInfoById(any(PaymentCancelInfoDTO.class));
    }
}
